package com.dentalmoovi.website.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.security.jwt.JWTprovider;
import com.dentalmoovi.website.services.UserSer;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/public")
public class AuthController {
    private final JWTprovider jWTprovider;
    private final UserSer userSer;
    private final AuthenticationManager am;
    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(JWTprovider jWTprovider, UserSer userSer, AuthenticationManager am) {
        this.jWTprovider = jWTprovider;
        this.userSer = userSer;
        this.am = am;
    }

    @Value("${jwt.accessTokenByCookieName}")
    private String cookieName;

    @Value("${server.domainName}")
    private String domain;

    @PostMapping("/isAuthorized")
    public ResponseEntity<Boolean> checkRole(@RequestBody LoginDTO loginUser) {

        String userDetails = userSer.getUserDetails(loginUser.userName());
        boolean isAdmin = userDetails.charAt(0) == 'A';

        if (isAdmin) {
            try {
                
                getAuth(loginUser.userName(), loginUser.password());

                String subject = "Codigo de inicio de sesión";
                String body =   
                "Querido Admin, Dental Moovi recibió una solicitud de inicio de sesión.\n\n"+
                "Si no realizo ningun inicio de sesión por favor comuniquelo inmediatamente al numero 314-453-6435\n\n"+
                "El codigo de confirmación es: ";

                userSer.sendEmailNotification(loginUser.userName(), subject, body);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }

        return ResponseEntity.ok(isAdmin);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login( HttpServletResponse hsr,
        @Valid @RequestBody LoginDTO loginUser, BindingResult bidBindingResult){

        if(bidBindingResult.hasErrors())
            return new ResponseEntity<>(new MessageDTO("Revise sus credenciales"), HttpStatus.BAD_REQUEST);
        try {
            String userDetails = userSer.getUserDetails(loginUser.userName());
            if(userDetails.charAt(0) == 'A'){
                userSer.validateCode(loginUser.userName(), loginUser.code());
            }

            Authentication auth = getAuth(loginUser.userName(), loginUser.password());
            SecurityContextHolder.getContext().setAuthentication(auth);
            String jwt = jWTprovider.generateToken(auth);
            Utils.createCookie(hsr, cookieName, jwt, false, -1, domain);
            return ResponseEntity.ok(new MessageDTO(userDetails.substring(1)));
        } catch (Exception e) {
            logger.error("Error to login: {}", e.getMessage());
            return new ResponseEntity<>(new MessageDTO("Revise sus credenciales "+e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<MessageDTO> logOut(HttpServletResponse hsr){
        Utils.clearCookie(hsr, cookieName);
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(new MessageDTO("Sesión cerrada"), HttpStatus.OK);
    }

    @Cacheable("auth")
    private Authentication getAuth(String userName, String password){
        return am.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
    }
}

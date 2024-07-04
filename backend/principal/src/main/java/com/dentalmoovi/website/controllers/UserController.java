package com.dentalmoovi.website.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dentalmoovi.website.models.dtos.AddressesDTO;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.responses.AddressesResponse;
import com.dentalmoovi.website.security.LoginDTO;
import com.dentalmoovi.website.security.PwDTO;
import com.dentalmoovi.website.services.UserSer;

@RestController
@RequestMapping
public class UserController {
    private final UserSer userSer;

    public UserController(UserSer userSer) {
        this.userSer = userSer;
    }

    @PostMapping("/api/public/create")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO){
        try {
            userSer.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping("/api/public/sendEmail")
    public void sendMessage(@RequestBody String email){
        String subject = "Codigo de confirmación";
        String body = "Dental Moovi recibió una solicitud.\n\n"+
                        "El codigo de confirmación es: ";
        userSer.sendEmailNotification(email, subject, body);
    }

    @GetMapping("/api/public/{email}/{signup}")
    public boolean checkEmailExists(@PathVariable("email") String email, @PathVariable("signup") boolean signup) {
        return userSer.checkEmailExists(email, signup);
    }
    
    @GetMapping("/api/user/getUser")
    public ResponseEntity<UserDTO> getUserAuthenticated(){
        try {
            UserDTO userDTO = userSer.getUserAuthDTO();
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    
    @PutMapping("/api/user/update")
    public ResponseEntity<MessageDTO> updateUser(@RequestBody UserDTO userDTO){
        try {
            return ResponseEntity.ok(userSer.updateUserInfo(userDTO));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/api/user/addAddress")
    public ResponseEntity<MessageDTO> createAddress(@RequestBody AddressesDTO addressDTO){
        try {
            userSer.createAddress(addressDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/api/user/getAddresses")
    public ResponseEntity<AddressesResponse> getAddresses(){
        try {
            return ResponseEntity.ok(userSer.getAddresses());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/api/user/updateAddress")
    public ResponseEntity<MessageDTO> updateAddress(@RequestBody AddressesDTO addressDTO){
        try {
            return ResponseEntity.ok(userSer.updateAddress(addressDTO));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/api/user/deleteAddress/{id}")
    public ResponseEntity<MessageDTO> deleteAddress(@PathVariable("id") long id){
        try{
            return ResponseEntity.ok(userSer.deleteAddress(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/user/name")
    public ResponseEntity<MessageDTO> getName(){
        return ResponseEntity.ok(userSer.getName());
    }

    @PutMapping("/api/user/upw")
    public ResponseEntity<MessageDTO> updatePw(
        @RequestBody PwDTO pwDto){
        try {
            return ResponseEntity.ok(userSer.changePw(pwDto));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/api/public/rpw")
    public ResponseEntity<MessageDTO> rememberPw(
        @RequestBody LoginDTO userCredentials){
        try {
            return ResponseEntity.ok(userSer.rememberPw(userCredentials));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}

package com.dentalmoovi.website.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JWTentryPoint implements AuthenticationEntryPoint{

    private static Logger logger = LoggerFactory.getLogger(JWTentryPoint.class);

    @Override
    //this method controls the error when the user doesn't have any authorizations
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException {
        
        String errorMessage = e.getMessage();
        if(errorMessage != null && !errorMessage.isEmpty()){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            logger.error(e.getMessage());
        }else{
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
        }
        
    }
}

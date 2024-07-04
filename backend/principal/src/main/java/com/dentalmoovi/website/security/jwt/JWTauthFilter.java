package com.dentalmoovi.website.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.security.UserRetrievalService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTauthFilter extends OncePerRequestFilter{
    private static Logger loggerConsole = LoggerFactory.getLogger(JWTauthFilter.class);

    private JWTprovider jWTprovider;
    private UserRetrievalService userRetrievalService;

    @Value("${jwt.accessTokenByCookieName:#{null}}")
    @NonNull private String cookieName = "";

    @Autowired
    public void dependencies(JWTprovider jWTprovider, UserRetrievalService userRetrievalService) {
        this.jWTprovider = jWTprovider;
        this.userRetrievalService = userRetrievalService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = Utils.getToken(request, cookieName);
            if (token != null && jWTprovider.validateToken(token)) {
                String userName = jWTprovider.getUserNameFromToken(token);
                UserDetails userRetrieval = userRetrievalService.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userRetrieval, null, userRetrieval.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            loggerConsole.error("error token: {}", e.getMessage());
            Utils.clearCookie(response, cookieName);
        } finally {
            filterChain.doFilter(request, response);
        }
    }

}

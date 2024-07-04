package com.dentalmoovi.website.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;

import com.dentalmoovi.website.models.entities.enums.RolesList;
import com.dentalmoovi.website.security.jwt.JWTauthFilter;
import com.dentalmoovi.website.security.jwt.JWTentryPoint;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTentryPoint jWTentryPoint;
    
    @Value("${server.domainUrl}")
    private String urlDomain;
    
    public SecurityConfig(JWTentryPoint jWTentryPoint) {
        this.jWTentryPoint = jWTentryPoint;
    }

    @Bean
    public SecurityFilterChain sfc(HttpSecurity http) throws Exception{

        return http
            .csrf(csrf -> csrf.disable())
            .cors(cors ->
                cors.configurationSource(request-> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of(urlDomain));
                    config.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH","OPTIONS"));
                    config.setAllowCredentials(true);
                    config.addExposedHeader("Message");
                    config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
                    return config;
                })
            )
            .authorizeHttpRequests(authRequest ->authRequest
                    .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                    .requestMatchers("/public/**").permitAll()
                    .requestMatchers("/api/public/**").permitAll()
                    .requestMatchers("/favicon.ico").permitAll()
                    .requestMatchers("/api/user/**").hasAuthority(RolesList.USER_ROLE.name())
                    .requestMatchers("/api/admin/**").hasAuthority(RolesList.ADMIN_ROLE.name())
                    //.anyRequest().authenticated()
            )
            .exceptionHandling(exception ->
                exception.authenticationEntryPoint(jWTentryPoint)
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jWTauthFilter(), UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public JWTauthFilter jWTauthFilter(){
        return new JWTauthFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration ac) throws Exception{
        return ac.getAuthenticationManager();
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher(){
        return new HttpSessionEventPublisher();
    }
}

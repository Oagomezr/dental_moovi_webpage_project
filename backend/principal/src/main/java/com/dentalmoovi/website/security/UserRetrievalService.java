package com.dentalmoovi.website.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dentalmoovi.website.services.UserSer;

@Service
public class UserRetrievalService implements UserDetailsService{
    private final UserSer userSer;

    public UserRetrievalService(UserSer userSer) {
        this.userSer = userSer;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userSer.getMainUser(email);
    }

}

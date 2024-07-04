package com.dentalmoovi.website.security;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.repositories.RolesRep;

public class MainUser implements UserDetails{

    private String cacheRef;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public MainUser(String cacheRef, String email, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.cacheRef = cacheRef;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static MainUser build(Users user, RolesRep rolesRep, String ref){
        Set<Long> rolesIds = user.getRolesIds();

        List<Roles> roles = rolesIds.stream()
            .map(idRole -> rolesRep.findById(idRole)
                .orElseThrow(() -> new RuntimeException("Role not found")))
            .collect(Collectors.toList());

        List<GrantedAuthority> authorities = roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.role().name()))
            .collect(Collectors.toList());

        String cacheRef = Utils.generateRandomString(2)+ String.valueOf(user.id() + ref.length())+Utils.generateRandomString(2);

        return new MainUser(cacheRef, user.email(), user.password(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    public String getCacheRef(){ return cacheRef; }

}

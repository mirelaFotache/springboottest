package com.pentalog.bookstore.utils;

import com.pentalog.bookstore.persistence.entities.User;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Profile("SECURITY_MOCK")
@Primary
@Service
public class AuthenticationProviderMock implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) {
        User user = new User();
        user.setUserName(authentication.getPrincipal().toString());
        user.setPassword(authentication.getCredentials().toString());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(user, "", mockGrantedAuthorities());
        usernamePasswordAuthenticationToken.setDetails(user);
        return usernamePasswordAuthenticationToken;
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return true;
    }

    private List<GrantedAuthority> mockGrantedAuthorities() {
        List<GrantedAuthority> persistedRoles = new ArrayList<>();
        persistedRoles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return persistedRoles;
    }
}

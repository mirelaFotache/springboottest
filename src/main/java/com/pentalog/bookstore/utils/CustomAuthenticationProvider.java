package com.pentalog.bookstore.utils;

import com.pentalog.bookstore.dto.RoleDTO;
import com.pentalog.bookstore.dto.UserDTO;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws BookstoreException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Use the credentials and authenticate against the third-party system
        Collection<UserDTO> users = userService.findByUserNameAndPassword(name, password);
        if (users != null && users.size() > 0) {
            UserDTO user = users.iterator().next();
            if (user.getUserName().equals(name) && user.getPassword().equals(password)) {

                List<GrantedAuthority> persistedRoles = new ArrayList<>();
                if (user.getUserRoles() != null && user.getUserRoles().size() > 0) {
                    for (RoleDTO role : user.getUserRoles())
                        persistedRoles.add(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()));
                }
                return new UsernamePasswordAuthenticationToken(name, password, persistedRoles);
            } else {
                throw new
                        BadCredentialsException("External system authentication failed");
            }
        }
        throw new
                BadCredentialsException("External system authentication failed");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
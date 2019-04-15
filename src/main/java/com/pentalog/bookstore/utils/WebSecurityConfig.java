package com.pentalog.bookstore.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/categories/**", "/books/**", "/bookings/**", "/ratings/**,/users/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/categories/**", "/books/**", "/bookings/**", "/ratings/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/categories/**", "/books/**", "/bookings/**", "/ratings/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/categories/**", "/books/**", "/bookings/**", "/ratings/**").hasRole("ADMIN")
                .antMatchers("/users/**", "/roles/**").hasRole("ADMIN")

                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
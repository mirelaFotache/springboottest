package com.pentalog.bookstore.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/categories/**","/books/**","/bookings/**","/ratings/**").hasAnyRole("ADMIN","USER")
                .antMatchers(HttpMethod.POST, "/categories/**","/books/**","/bookings/**","/ratings/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/categories/**","/books/**","/bookings/**","/ratings/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/categories/**","/books/**","/bookings/**","/ratings/**").hasRole("ADMIN")
                .antMatchers("/users/**","/roles/**").hasRole("ADMIN")

                .anyRequest().authenticated()

                .and()
                .formLogin()
                .and()
                .logout()
                .and()
                .httpBasic();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user").password(encoder.encode("user")).roles("USER").build());
        manager.createUser(User.withUsername("admin").password(encoder.encode("admin")).roles("ADMIN").build());
        return manager;
    }

}
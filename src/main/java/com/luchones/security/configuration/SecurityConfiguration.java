package com.luchones.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean   
    public SecurityFilterChain filterChain(final HttpSecurity http,
                                           final LogoutHandler logoutHandler) throws Exception {


        http.authorizeHttpRequests((requests) -> requests
            .anyRequest().authenticated()
        ).oauth2Login(config -> {

        try {
            config.configure(http);
        } catch (Exception exception) {

            exception.printStackTrace();
        }
        }).logout(config -> 
            config.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .addLogoutHandler(logoutHandler)
        );

        return http.build();
    }

}

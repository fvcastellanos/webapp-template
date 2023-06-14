package com.luchones.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.luchones.security.handler.SuccessLoginHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean   
    public SecurityFilterChain filterChain(final HttpSecurity http,
                                           final LogoutHandler logoutHandler,
                                           final SuccessLoginHandler successLoginHandler) throws Exception {

        http.authorizeHttpRequests((requests) -> requests
            .anyRequest().authenticated())
            .oauth2Login(config -> {
                config.successHandler(successLoginHandler);
            })
            .logout(config -> 
                config.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                      .addLogoutHandler(logoutHandler)
            );

        return http.build();
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(PermissionEvaluator permissionEvaluator) {

        var expressionHandler = new DefaultMethodSecurityExpressionHandler();
	    expressionHandler.setPermissionEvaluator(permissionEvaluator);
        
        return expressionHandler;
    }

}

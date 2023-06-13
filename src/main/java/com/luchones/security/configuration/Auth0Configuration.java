package com.luchones.security.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auth0.client.auth.AuthAPI;

@Configuration
public class Auth0Configuration {
    
    @Bean
    public AuthAPI authAPI(@Value("${AUTH0_DOMAIN}") final String domain,
                           @Value("${AUTH0_CLIENT_ID}") final String clientId,
                           @Value("${AUTH0_CLIENT_SECRET}") final String clientSecret) {

        return AuthAPI.newBuilder(domain, clientId, clientSecret)
                            .build();
    }
}

package com.luchones.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class Auth0LogoutHandler implements LogoutHandler {

    private static final String REGISTRATION_ID = "auth0";

    private final ClientRegistrationRepository clientRegistrationRepository;

    public Auth0LogoutHandler(final ClientRegistrationRepository clientRegistrationRepository) {

        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    public void logout(final HttpServletRequest request, 
                       final HttpServletResponse response, 
                       final Authentication authentication) {

        if (request.getSession() != null) {

            request.getSession()
                .invalidate();
        }

        final var issuer = (String) getClientRegistration()
            .getProviderDetails()
            .getConfigurationMetadata()
            .get("issuer");

        final var clientId = getClientRegistration().getClientId();
        final var returnTo = ServletUriComponentsBuilder.fromCurrentContextPath()
            .build()
            .toString();

        final var logoutUrl = UriComponentsBuilder.fromHttpUrl(issuer + "v2/logout?client_id={clientId}&returnTo={returnTo}")
                .encode()
                .buildAndExpand(clientId, returnTo)
                .toUriString();        

        try {

            response.sendRedirect(logoutUrl);

        } catch (IOException ioException) {

            ioException.printStackTrace();
        }
        
    }

    private ClientRegistration getClientRegistration() {

        return clientRegistrationRepository.findByRegistrationId(REGISTRATION_ID);
    }    
}

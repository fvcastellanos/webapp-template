package com.luchones.security.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(Auth0LogoutHandler.class);

    private static final String REGISTRATION_ID = "auth0";

    private final ClientRegistrationRepository clientRegistrationRepository;

    public Auth0LogoutHandler(final ClientRegistrationRepository clientRegistrationRepository) {

        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    public void logout(final HttpServletRequest request, 
                       final HttpServletResponse response, 
                       final Authentication authentication) {

        LOGGER.info("Performing logout for user: {}", authentication.getPrincipal());

        if (request.getSession() != null) {

            LOGGER.info("Invalidate Session");

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

            LOGGER.info("Redirecting to logout URL");
            response.sendRedirect(logoutUrl);

        } catch (IOException ioException) {

            LOGGER.error("Unable to logout user: {} - ", authentication.getPrincipal(), ioException);
        }
        
    }

    private ClientRegistration getClientRegistration() {

        return clientRegistrationRepository.findByRegistrationId(REGISTRATION_ID);
    }    
}

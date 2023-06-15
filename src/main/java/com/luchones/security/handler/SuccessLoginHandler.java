package com.luchones.security.handler;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SuccessLoginHandler implements AuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuccessLoginHandler.class);

    private final Auth0ManagementHandler auth0ManagementHandler;

    public SuccessLoginHandler(final Auth0ManagementHandler auth0ManagementHandler) {

        this.auth0ManagementHandler = auth0ManagementHandler;
    }

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, 
                                        final HttpServletResponse response,
                                        final Authentication authentication) throws IOException, ServletException {

            final var oauth2User = (OAuth2User) authentication.getPrincipal();
            final var userId = oauth2User.getName();

            LOGGER.info("Success authentication for user: {}", userId);

            final var userRoles = auth0ManagementHandler.readUserRoles(userId);
            request.getSession().setAttribute("roles", userRoles);

            response.sendRedirect("/");
    }
    
}

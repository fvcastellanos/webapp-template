package com.luchones.security.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;

@Component
public class Auth0ManagementHandler {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(Auth0ManagementHandler.class);

    private final AuthAPI authAPI;
    private final String domain;

    public Auth0ManagementHandler(final AuthAPI authAPI,
                                  @Value("${AUTH0_DOMAIN}") final String domain) {

        this.authAPI = authAPI;
        this.domain = domain;
    }

    public List<String> readUserRoles(final String userId) throws Auth0Exception {

        try {
            final var managementAPI = buildManagementAPI(domain);

            LOGGER.info("Loading associated roles of user: {}", userId);

            var assignedRoles = managementAPI.users()
                                    .listRoles(userId, null)
                                    .execute()
                                    .getBody();


            LOGGER.info("Assigned Roles: {}", assignedRoles.getLength());

            return assignedRoles.getItems().stream()
                .map(role -> role.getName())
                .toList();

        } catch (final Auth0Exception auth0Exception) {

            LOGGER.error("Unable to retrieve user: {} roles", auth0Exception);
            throw auth0Exception;
        }
    }

    private ManagementAPI buildManagementAPI(final String domain) throws Auth0Exception {

        LOGGER.info("Build Management API");

        final var apiURL = String.format("https://%s/api/v2/", domain);

        final var tokenRequest = authAPI.requestToken(apiURL);

        final var holder = tokenRequest.execute()
                                        .getBody();

        final var accessToken = holder.getAccessToken();

        return ManagementAPI.newBuilder(domain, accessToken)
                    .build();        
    }    
}

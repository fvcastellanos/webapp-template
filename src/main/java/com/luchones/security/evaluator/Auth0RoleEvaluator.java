package com.luchones.security.evaluator;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class Auth0RoleEvaluator implements PermissionEvaluator {

    private static final String CLAIM_NAME = "net.cavitos.app.roles";

    @Override
    public boolean hasPermission(final Authentication authentication, 
                                 final Object targetDomainObject, 
                                 final Object permission) {

        if (Objects.isNull(authentication) || Objects.isNull(targetDomainObject) || !(permission instanceof String)) {
            return false;
        }

        final var targetType = targetDomainObject.getClass()
                                    .getSimpleName()
                                    .toUpperCase();
        
        return hasClaim(authentication, targetType, permission.toString().toUpperCase());        
    }

    @Override
    public boolean hasPermission(final Authentication authentication, 
                                 final Serializable targetId, 
                                 final String targetType,
                                 final Object permission) {

        if (Objects.isNull(authentication) || Objects.isNull(targetType) || !(permission instanceof String)) {
            return false;
        }                                    

        return hasClaim(authentication, targetType, targetType);
    }

    private boolean hasClaim(final Authentication authentication, 
                             final String targetType, 
                             final String permission) {

        final OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        try {
            final var attributes = (List<String>) (oAuth2User.getAttribute(CLAIM_NAME));

            return attributes.stream()
                    .map(String::toUpperCase)
                    .anyMatch(item -> item.equals(permission.toUpperCase()));
        } catch (ClassCastException castException) {

            return false;
        }
    }
    
}

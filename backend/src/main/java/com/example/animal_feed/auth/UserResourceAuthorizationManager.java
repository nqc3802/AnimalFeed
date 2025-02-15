package com.example.animal_feed.auth;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.example.animal_feed.user.CustomUserDetails;

@Component
public class UserResourceAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private static final List<String> PROTECTED_PATHS = List.of(
            "/api/v1/cart/", "/api/v1/orders/", "/api/v1/reviews/", "/api/v1/user/");

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        var auth = Optional.ofNullable(authentication.get());

        if (auth.isEmpty() || !(auth.get().getPrincipal() instanceof CustomUserDetails)) {
            return new AuthorizationDecision(false);
        }

        String requestURI = context.getRequest().getRequestURI();

        if (PROTECTED_PATHS.stream().noneMatch(requestURI::startsWith)) {
            return new AuthorizationDecision(true);
        }

        return new AuthorizationDecision(true);
    }
}

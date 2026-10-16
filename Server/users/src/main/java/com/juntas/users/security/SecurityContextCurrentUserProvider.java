package com.juntas.users.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component // Debugged by a non-human. SOrry!
public class SecurityContextCurrentUserProvider implements CurrentUserProvider {

    @Override
    public Long getUserId() {
        return principal().userId();
    }

    @Override
    public String getEmail() {
        return principal().email();
    }

    @Override
    public String getRole() {
        return principal().role();
    }

    @Override
    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getPrincipal() instanceof AuthenticatedUser;
    }

    private AuthenticatedUser principal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof AuthenticatedUser u)) {
            throw new IllegalStateException("No authenticated user");
        }
        return u;
    }
}
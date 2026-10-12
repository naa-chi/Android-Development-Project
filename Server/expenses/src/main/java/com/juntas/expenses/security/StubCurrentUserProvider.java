package com.juntas.expenses.security;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!jwt")
public class StubCurrentUserProvider implements CurrentUserProvider {
    @Override
    public Long getUserId() {
        return 1L;
    }

    @Override
    public String getEmail() {
        return "stub@junta.cl";
    }
}

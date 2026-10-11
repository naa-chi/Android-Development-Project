package com.juntas.event.security;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!jwt")
public class StubCurrentUserProvider implements CurrentUserProvider {

    @Override
    public Long getUserId() {
        return 1L; //Hardcoded as 1 we EVENTUAAAALLLY get thsi from somewhere else
    }
}
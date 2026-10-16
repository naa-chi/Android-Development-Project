package com.juntas.event.security;

public interface CurrentUserProvider {
    Long getUserId();
    String getEmail();
    String getRole();
    boolean isAuthenticated();
}
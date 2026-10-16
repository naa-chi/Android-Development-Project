package com.juntas.users.security;

public interface CurrentUserProvider {
    Long getUserId();
    String getEmail();
    String getRole();
    boolean isAuthenticated();
}
package com.juntas.expenses.security;

public interface CurrentUserProvider {
    Long getUserId();
    String getEmail();
}

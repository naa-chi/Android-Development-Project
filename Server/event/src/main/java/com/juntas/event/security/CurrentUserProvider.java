package com.juntas.event.security;

// We hardcode this, eventually we actually get this from somewhere, somehow. 
public interface CurrentUserProvider {
    Long getUserId();
}
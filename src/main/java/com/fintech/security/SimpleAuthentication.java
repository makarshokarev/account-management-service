package com.fintech.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class SimpleAuthentication implements Authentication {

    // TODO: This is a mock implementation for demo purposes

    @Override
    public String getName() { return "fakeUser"; }

    @Override
    public Object getPrincipal() { return "fakeUser"; }

    @Override
    public Object getCredentials() { return null; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("USER_READ"),
                new SimpleGrantedAuthority("USER_WRITE")
        );
    }

    @Override
    public boolean isAuthenticated() { return true; }

    @Override
    public void setAuthenticated(boolean authenticated) {}

    @Override
    public Object getDetails() { return null; }
}

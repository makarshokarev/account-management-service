package com.fintech.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class FakeAuthFilter extends GenericFilterBean {

    // TODO: This filter simulates authenticated user for demo purposes

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        SecurityContextHolder.getContext().setAuthentication(new SimpleAuthentication());
        filterChain.doFilter(request, response);
    }
}

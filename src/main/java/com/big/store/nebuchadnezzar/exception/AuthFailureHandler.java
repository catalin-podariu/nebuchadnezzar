package com.big.store.nebuchadnezzar.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthFailureHandler implements AuthenticationFailureHandler {

    private final Map<String, Integer> attempts = new HashMap<>();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String username = request.getParameter("username");
        int attempt = attempts.getOrDefault(username, 0) + 1;
        if (attempt >= 3) {
            // Lock the account or take other appropriate action
            // For example, you can set the account status to "LOCKED"
        } else {
            attempts.put(username, attempt);
        }
        response.sendRedirect("/login?error");
    }
}

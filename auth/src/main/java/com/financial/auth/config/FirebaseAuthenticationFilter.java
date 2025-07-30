package com.financial.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import org.financial.common.security.FirebaseAuthenticationFilterBase;

public class FirebaseAuthenticationFilter extends FirebaseAuthenticationFilterBase {

    @Override
    protected boolean shouldSkipFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/auth/");
    }
}

package org.financial.common.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
@Configuration
public class SecuritySharedConfig {

    public static HttpSecurity applyCommonSecurity(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable);
    }
}

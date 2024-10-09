package com.esoft.citytaxi.util;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * The type of Audit aware bean to get the username from spring security context
 *
 */
@Component
public class AuditAwareBean {

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}

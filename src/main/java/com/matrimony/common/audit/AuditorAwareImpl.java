package com.matrimony.common.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        // Example for now
        // Later you can extract from Spring Security Context
        return Optional.of("SYSTEM");
    }
}
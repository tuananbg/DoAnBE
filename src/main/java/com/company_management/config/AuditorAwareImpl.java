package com.company_management.config;

import com.company_management.controller.auth.BaseController;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl extends BaseController implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(getCurrentUserCode() == null ? "SYSTEM" : getCurrentUserCode());
    }
}

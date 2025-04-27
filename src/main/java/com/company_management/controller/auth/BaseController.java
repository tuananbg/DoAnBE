package com.company_management.controller.auth;

import com.company_management.dto.au.EmployeeInfo;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseController {
    @Value("${apiPrefix}")
    private String apiPrefix;

    private static final String UNKNOW_USER = "anonymousUser";

    private static final String DEFAULT_AUDITOR = "System";

    protected EmployeeInfo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (authentication == null || authentication.getName().equals(UNKNOW_USER)) ? null
                : (EmployeeInfo) authentication.getPrincipal();
    }

    protected String getCurrentUserCode() {
        EmployeeInfo currentUser = getUserInfo();
        if (currentUser == null)
            return DEFAULT_AUDITOR;

        return StringUtils.isEmpty(currentUser.getEmployeeCode()) ? DEFAULT_AUDITOR : currentUser.getEmployeeCode();
    }

}

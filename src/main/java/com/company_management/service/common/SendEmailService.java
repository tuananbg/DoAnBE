package com.company_management.service.common;

import com.company_management.common.enums.EmailTemplate;

public interface SendEmailService {
    void sendEmail(String code, EmailTemplate emailTemplate, long id);
}

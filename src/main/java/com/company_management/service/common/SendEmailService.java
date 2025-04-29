package com.company_management.service.common;

import com.company_management.common.enums.EmailTemplate;
import com.company_management.entity.Account;

public interface SendEmailService {
    void sendEmailAttendance(String code, EmailTemplate emailTemplate, long id);

    void sendEmailForAccount(Account account,EmailTemplate emailTemplate);
}

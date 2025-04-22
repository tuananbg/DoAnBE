package com.company_management.service;

import com.company_management.dto.common.MailRequest;

public interface EmailService {
    void send(MailRequest mail);
}

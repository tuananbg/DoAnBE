package com.company_management.service;

import com.company_management.dto.request.MailRequest;

public interface EmailService {
    void send(MailRequest mail);
}

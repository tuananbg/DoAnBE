package com.company_management.service.au;

import com.company_management.dto.au.ForgotPasswordRequest;
import com.company_management.dto.au.RequestChangePasswordDTO;
import com.company_management.dto.au.RequestLoginDTO;
import com.company_management.dto.response.au.ResponseLoginDTO;

public interface AuthorService {
    ResponseLoginDTO login(RequestLoginDTO request);

    void changePassword(RequestChangePasswordDTO request);

    void logout(String account);

    void forgotPassword(ForgotPasswordRequest request);

    Boolean checkVerifyCode(String otp);

    void resendVerifyCode(String account) ;
}

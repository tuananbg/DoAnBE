package com.company_management.service;

import com.company_management.dto.request.AuthenticationRequest;
import com.company_management.dto.request.ChangePasswordRequest;

import com.company_management.dto.request.RegisterRequest;
import com.company_management.dto.response.AuthenticationResponse;
import com.company_management.dto.response.BasicResponse;


import java.security.Principal;

public interface AuthenticationService {

     void register(RegisterRequest request);

     AuthenticationResponse authenticate(AuthenticationRequest request);

     boolean activeAccount(String activeCode);

     Boolean changePassword(ChangePasswordRequest request, Principal principal) ;

    Boolean forgotPassword(String email);

     AuthenticationResponse validForgotCode(String forgotCode);

     BasicResponse resendVerifyCode(Long id) ;
}

//package com.company_management.service.au;
//
//import com.company_management.dto.au.RequestLoginDTO;
//import com.company_management.dto.au.ChangePasswordRequest;
//
//import com.company_management.dto.au.RegisterRequest;
//import com.company_management.dto.response.au.ResponseLoginDTO;
//import com.company_management.dto.common.BasicResponse;
//
//
//import java.security.Principal;
//
//public interface AuthenticationService {
//
//     void register(RegisterRequest request);
//
//     ResponseLoginDTO authenticate(RequestLoginDTO request);
//
//     boolean activeAccount(String activeCode);
//
//     Boolean changePassword(ChangePasswordRequest request, Principal principal) ;
//
//    Boolean forgotPassword(String email);
//
//     ResponseLoginDTO validForgotCode(String forgotCode);
//
//     BasicResponse resendVerifyCode(Long id) ;
//}

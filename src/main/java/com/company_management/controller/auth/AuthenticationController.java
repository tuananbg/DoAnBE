package com.company_management.controller.auth;

import com.company_management.common.AppConstants;
import com.company_management.common.ErrorCode;
import com.company_management.common.ResultResp;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.request.AuthenticationRequest;
import com.company_management.dto.request.ChangePasswordRequest;
import com.company_management.dto.request.RegisterRequest;
import com.company_management.dto.response.AuthenticationResponse;
import com.company_management.service.AuthenticationService;
import com.company_management.dto.response.BasicResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("${apiPrefix}/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public BaseResponse<Object> register(@RequestBody RegisterRequest request) {
        authenticationService.register(request);
        return BaseResponse.ok(AppConstants.STATUS_201, AppConstants.EMPLOYEE_201);
    }

    @PostMapping("/login")
    public BaseResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return BaseResponse.ok(authenticationService.authenticate(request));
    }

    @GetMapping("register/{activeCode}")
    public ResponseEntity<BasicResponse> activeAccount(@PathVariable String activeCode) {
        if (authenticationService.activeAccount(activeCode)) {
            return new ResponseEntity<>(new BasicResponse(200, "Kích hoạt tài khoản thành công"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new BasicResponse(400, "Mã kích hoạt không đúng"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<Boolean> forgotPassword(@RequestParam("email") String email) {
        return new ResponseEntity<>(authenticationService.forgotPassword(email), HttpStatus.OK);
    }

    @PostMapping("/forgot-password/{forgotCode}")
    public ResponseEntity<AuthenticationResponse> validForgotCode(@PathVariable String forgotCode) {
        return new ResponseEntity<>(authenticationService.validForgotCode(forgotCode), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<BasicResponse> changePassword(@RequestBody ChangePasswordRequest request, Principal principal) {
        if (authenticationService.changePassword(request, principal)) {
            return new ResponseEntity<>(new BasicResponse(200, "Thay đổi mật khẩu thành công"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new BasicResponse(500, "Thay đổi mật khẩu không thành công"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/resend-code")
    private ResponseEntity<BasicResponse> resendCode(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(authenticationService.resendVerifyCode(id), HttpStatus.OK);
    }
}

package com.company_management.controller.auth;


import com.company_management.common.AppConstants;
import com.company_management.dto.au.ForgotPasswordRequest;
import com.company_management.dto.au.RequestChangePasswordDTO;
import com.company_management.dto.au.RequestLoginDTO;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.BasicResponse;
import com.company_management.dto.response.au.ResponseLoginDTO;
import com.company_management.service.au.AuthorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthorService authorService;

    @PostMapping("/login")
    public BaseResponse<ResponseLoginDTO> login(@RequestBody @Valid RequestLoginDTO request) throws JsonProcessingException {

        ResponseLoginDTO data = authorService.login(request);
        return BaseResponse.ok(data);
    }

    @GetMapping("/check-verify-code/{code}")
    public BaseResponse<Boolean> checkVerifyCode(@PathVariable("code") String code) {
        if (authorService.checkVerifyCode(code)) {
            return BaseResponse.ok(AppConstants.GET_CODE_200, AppConstants.GET_MESSAGE_200);
        }
        return BaseResponse.error(AppConstants.CODE_400, AppConstants.MESS_400);

    }

    @PostMapping("/resend-code/{account}")
    public BaseResponse<?> validForgotCode(@PathVariable("account") String account) {
        authorService.resendVerifyCode(account);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201, AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @PostMapping("/change-password")
    public BaseResponse<BasicResponse> changePassword(@RequestBody RequestChangePasswordDTO request) {
        authorService.changePassword(request);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202, AppConstants.UPDATE_SUCCESS_MESS_202);

    }

    @PostMapping("/forgot-password")
    public BaseResponse<BasicResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authorService.forgotPassword(request);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202, AppConstants.UPDATE_SUCCESS_MESS_202);

    }

}

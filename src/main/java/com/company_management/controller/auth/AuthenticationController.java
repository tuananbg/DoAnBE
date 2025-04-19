package com.company_management.controller.auth;


import com.company_management.dto.au.RequestLoginDTO;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.response.au.ResponseLoginDTO;
import com.company_management.service.au.AuthorService;
import com.company_management.service.au.impl.AuthorServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthorService authorService;
    private final AuthorServiceImpl authService;

    @PostMapping("/login")
    public BaseResponse<ResponseLoginDTO> login(@RequestBody @Valid RequestLoginDTO request) throws JsonProcessingException {

        ResponseLoginDTO data = authService.login(request);
        return BaseResponse.ok(data);
    }

//    @PostMapping("/register")
//    public BaseResponse<Object> register(@RequestBody RegisterRequest request) {
//        authorService.register(request);
//        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201, AppConstants.CREATE_SUCCESS_MESS_201);
//    }

//    @PostMapping("/login")
//    public BaseResponse<AuthenticationResponse> login(@RequestBody RequestLoginDTO request) {
//        return BaseResponse.ok(authenticationService.authenticate(request));
//    }

//    @GetMapping("register/{activeCode}")
//    public ResponseEntity<BasicResponse> activeAccount(@PathVariable String activeCode) {
//        if (authenticationService.activeAccount(activeCode)) {
//            return new ResponseEntity<>(new BasicResponse(200, "Kích hoạt tài khoản thành công"), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(new BasicResponse(400, "Mã kích hoạt không đúng"), HttpStatus.BAD_REQUEST);
//    }
//
//    @GetMapping("/forgot-password")
//    public ResponseEntity<Boolean> forgotPassword(@RequestParam("email") String email) {
//        return new ResponseEntity<>(authenticationService.forgotPassword(email), HttpStatus.OK);
//    }
//
//    @PostMapping("/forgot-password/{forgotCode}")
//    public ResponseEntity<ResponseLoginDTO> validForgotCode(@PathVariable String forgotCode) {
//        return new ResponseEntity<>(authenticationService.validForgotCode(forgotCode), HttpStatus.OK);
//    }
//
//    @PostMapping("/change-password")
//    public ResponseEntity<BasicResponse> changePassword(@RequestBody RequestChangePasswordDTO request) {
//        if (authorService.changePassword(request)) {
//            return new ResponseEntity<>(new BasicResponse(200, "Thay đổi mật khẩu thành công"), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(new BasicResponse(500, "Thay đổi mật khẩu không thành công"), HttpStatus.BAD_REQUEST);
//    }
//
//    @PostMapping("/resend-code")
//    private ResponseEntity<BasicResponse> resendCode(@RequestParam(name = "id") Long id) {
//        return new ResponseEntity<>(authenticationService.resendVerifyCode(id), HttpStatus.OK);
//    }
}

package com.company_management.service.impl;

import com.company_management.common.Constants;
import com.company_management.common.enums.Role;
import com.company_management.dto.request.AuthenticationRequest;
import com.company_management.dto.request.ChangePasswordRequest;
import com.company_management.dto.request.MailRequest;
import com.company_management.dto.request.RegisterRequest;
import com.company_management.dto.response.AuthenticationResponse;
import com.company_management.dto.response.BasicResponse;
import com.company_management.entity.UserAccount;
import com.company_management.exception.AppException;
import com.company_management.exception.BadRequestException;
import com.company_management.repository.UserAccountRepository;
import com.company_management.repository.EmployeeInfoRepository;
import com.company_management.service.AuthenticationService;
import com.company_management.service.EmailService;
import com.company_management.service.JwtService;
import com.company_management.utils.LogisticsMailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userAccountRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmployeeInfoRepository employeeInfoRepository;
    private final EmailService emailService;
    private final ITemplateEngine templateEngine;


    @Override
    public void register(RegisterRequest request) {
        UserAccount user = new UserAccount();
        if (userAccountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException("ERR01", "Email đã tồn tại!");
        }
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.findByCode(request.getRole()));
        user.setIsActive(Constants.STATUS_ACTIVE_INT);
        user.setActiveCode(generateCode());
        user.setCreatedBy("admin");
        user.setCreatedDate(new Date());
        userAccountRepository.save(user);
        Map<String, Object> params = LogisticsMailUtils.sendMailToCode(user.getActiveCode());
        Context context = new Context();
        context.setVariables(params);
        MailRequest mailRequest = MailRequest.builder()
                .toMail(user.getEmail())
                .html(true)
                .title("Công ty cổ phần truyền thông và dịch vụ DTDI")
                .content(templateEngine.process(MailRequest.CODE_REGISTER_PROVIDER_TEMPLATE, context))
                .build();
        emailService.send(mailRequest);
//        String jwtToken = jwtService.generateToken(user);
//        AuthenticationResponse response = new AuthenticationResponse();
//        response.setUsername(user.getUsername());
//        response.setToken(jwtToken);
//        response.setRoles(user.getRole().getCode());
//        return response;
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        UserAccount user =
                userAccountRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException(
                        "Không tìm thấy User có địa chỉ email" + request.getEmail()));
        if (!user.getIsActive().equals(Constants.STATUS_ACTIVE_INT)) {
            throw new RuntimeException("User chưa được kích hoạt");
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception ex) {
            throw new AppException("ERR02", "Email hoặc mật khẩu không đúng!");
        }
        String jwtToken = jwtService.generateToken(user);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setFullName(user.getUsername());
        response.setToken(jwtToken);
        response.setRoles(user.getRole().getCode());
        response.setEmail(user.getEmail());
        if (user.getEmployee() != null) {
            response.setEmployeeCode(user.getEmployee().getCode());
        } else {
            response.setEmployeeCode("");
        }

        return response;
    }


    @Override
    public boolean activeAccount(String activeCode) {
        UserAccount user = userAccountRepository.findByActiveCode(activeCode).orElseThrow(() -> new RuntimeException(
                "Wrong active code!!!"));
        user.setActiveCode(null);
        user.setIsActive(Constants.STATUS_ACTIVE_INT);
        userAccountRepository.save(user);
        return true;
    }


    @Override
    public Boolean forgotPassword(String email) {
        UserAccount user =
                userAccountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                        "User not found"));
        user.setForgotPassword(generateCode());
        userAccountRepository.save(user);
        return true;
    }

    @Override
    public AuthenticationResponse validForgotCode(String forgotCode) {
        UserAccount user =
                userAccountRepository.findByForgotPassword(forgotCode).orElseThrow(() -> new RuntimeException("User " +
                        "not found!!!"));
        String jwtToken = jwtService.generateToken(user);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setFullName(user.getUsername());
        response.setToken(jwtToken);
        response.setRoles(user.getRole().getCode());
        response.setEmail(user.getEmail());
        if (user.getEmployee() != null) {
            response.setEmployeeCode(user.getEmployee().getCode());
        } else {
            response.setEmployeeCode(" ");
        }

        return response;
    }

    @Override
    public Boolean changePassword(ChangePasswordRequest request, Principal principal) {
        UserAccount user =
                userAccountRepository.findByEmail(principal.getName()).orElseThrow(() -> new RuntimeException("Email " +
                        "not found!!!"));
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Confirm Password not same!!!");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userAccountRepository.save(user);
        return true;
    }

    @Override
    public BasicResponse resendVerifyCode(Long id) {
        UserAccount user =
                userAccountRepository.findById(id).orElseThrow(() -> new BadRequestException("Có lỗi " +
                        "xảy ra: Không tìm thấy User theo id: " + id));
        String verifyCode = generateCode();
        user.setActiveCode(verifyCode);
        userAccountRepository.save(user);
        //Todo: Gui email code
        return new BasicResponse(200, "Mã xác thực mới đã được gửi vào email: " + user.getEmail());
    }

    public String generateCode() {
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(48, 58) // Chỉ lấy số từ '0' (48) đến '9' (57)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}

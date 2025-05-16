package com.company_management.service.au.impl;

import com.company_management.common.enums.AuthorMessage;
import com.company_management.common.enums.ConfigDataCode;
import com.company_management.common.enums.EmailTemplate;
import com.company_management.common.enums.EmploymentStatus;
import com.company_management.config.AppConfig;
import com.company_management.dto.au.ForgotPasswordRequest;
import com.company_management.dto.au.EmployeeInfo;
import com.company_management.dto.au.RequestChangePasswordDTO;
import com.company_management.dto.au.RequestLoginDTO;
import com.company_management.dto.response.au.ResponseLoginDTO;
import com.company_management.entity.Account;
import com.company_management.entity.Employee;
import com.company_management.exception.AppException;
import com.company_management.repository.AccountRepository;
import com.company_management.service.au.AuthorService;
import com.company_management.service.common.SendEmailService;
import com.company_management.utils.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AppConfig appConfig;
    private final AuthenticationManager authenticationManager;
    private final JWTServiceImpl jwtService;
    private final HttpServletRequest req;
    private final AccountRepository accountRepository;
    private final SendEmailService sendEmailService;

    private static final String ADMIM = "ADMIN";

    @Override
    public ResponseLoginDTO login(RequestLoginDTO request) {
        ResponseLoginDTO result = new ResponseLoginDTO();
        String account = "";
        boolean emailFlag = false;
        if (request.getAccount().contains("@")) {
            // case login by email
            account = request.getAccount().split("@")[0];
            emailFlag = true;
        } else {
            // case login by account
            account = request.getAccount();
        }
        EmployeeInfo userDetails = (EmployeeInfo) userDetailsService.loadUserByUsername(account);
        if (userDetails.isSuperAdmin()) {
            return adminLogin(request, userDetails);
        }

        Employee em = userDetails.getEmployee();
        Account acc = userDetails.getAccount();
        if (emailFlag && !request.getAccount().equalsIgnoreCase(em.getEmployeeInfo().getEmail())) {
            throw new AppException(AuthorMessage.ACCOUNT_NOT_FOUND.getCode(),
                    AuthorMessage.ACCOUNT_NOT_FOUND.getMessage());
        }
        verifyPassword(request.getPassword(), userDetails.getPassword(), acc);
        String token = jwtService.generateToken(userDetails);
        result.setToken(token);
        result.setFullName(em.getFullName());
        result.setEmployeeCode(em.getCode());
        result.setEmail(em.getEmployeeInfo().getEmail());
        if (em.getRoles() != null) {
            List<String> roleCodes = new ArrayList<>();
           em.getRoles().forEach(role -> {
               roleCodes.add(role.getCode());
           });
            result.setRoles(roleCodes);
        }

        authenticate(userDetails.getUsername(), request.getPassword(), userDetails, token);
        return result;
    }

    private ResponseLoginDTO adminLogin(RequestLoginDTO request, EmployeeInfo userDetails) {
        ResponseLoginDTO result = new ResponseLoginDTO();
        // check password
        String password = ConfigDataCode.SYSTEM_ADMIN_PASSWORD;
        if (!passwordEncoder.matches(request.getPassword(), password)) {
            throw new AppException(AuthorMessage.WRONG_PASSWORD.getCode(),
                    AuthorMessage.WRONG_PASSWORD.getMessage());
        }
        String token = jwtService.generateToken(userDetails);
        result.setToken(token);
        result.setFullName(EmployeeInfo.SUPER_ADMIN);
        result.setEmployeeCode(ADMIM);
        result.setRoles(List.of(ADMIM));
        authenticate(userDetails.getUsername(), request.getPassword(), userDetails, token);
        return result;
    }

    private void authenticate(String username, String password, EmployeeInfo userDetails, String token) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        userDetails.setToken(token);
    }

    @Override
    public void changePassword(RequestChangePasswordDTO request) {

        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            // duplicate password
            throw new AppException(AuthorMessage.DUPLICATE_WITH_CURRENT_PASSWORD.getCode(),
                    AuthorMessage.DUPLICATE_WITH_CURRENT_PASSWORD.getMessage());
        }
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            // confirm password not match
            throw new AppException(AuthorMessage.CONFIRM_PASSWORD_NOT_MATCH.getCode(),
                    AuthorMessage.CONFIRM_PASSWORD_NOT_MATCH.getMessage());
        }

        if (request.getAccount().equals(EmployeeInfo.SUPER_ADMIN)) {
            // case administrator then change password in config table
            String password = ConfigDataCode.SYSTEM_ADMIN_PASSWORD;
            if (!passwordEncoder.matches(request.getCurrentPassword(),password)) {
                throw new AppException(AuthorMessage.WRONG_CURENT_PASSWORD.getCode(),
                        AuthorMessage.WRONG_CURENT_PASSWORD.getMessage());
            }
        } else {
            // get employee
            Account account = accountRepository.findByAccountIgnoreCase(request.getAccount());
            if (account == null) {
                throw new AppException("ERR","Tài khoản không tồn tại");
            }
            // check current password is valid or not
            if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
                throw new AppException(AuthorMessage.WRONG_CURENT_PASSWORD.getCode(),
                        AuthorMessage.WRONG_CURENT_PASSWORD.getMessage());
            }
            // change password
            account.setPassword(passwordEncoder.encode(request.getNewPassword()));
            account.setNumPwWrong(0);
            Calendar c = Calendar.getInstance();
            c.setTime(DateUtils.getNow());
            // get expire password config

            c.add(Calendar.DATE, ConfigDataCode.SYSTEM_EXPIRED_PASSWORD);
            account.setPwExpDate(c.getTime());
            accountRepository.save(account);
        }
    }

    @Override
    public void logout(String account) {
        // TODO
    }


    private void verifyPassword(String requestPw, String userPw, Account acc) {
        if (!passwordEncoder.matches(requestPw, userPw)) {

            Integer numPassWrong = acc.getNumPwWrong();
            if (numPassWrong == null) {
                numPassWrong = 1;
            } else {
                numPassWrong = numPassWrong + 1;
            }

            int maxNumPassWrong = ConfigDataCode.SYSTEM_NUM_PASSWORD_WRONG;
            if (numPassWrong == maxNumPassWrong) {
                // login wrong password many time then lock user
                acc.setStatus(EmploymentStatus.LOCK.getCode());
                acc.setNumPwWrong(0);
                throw new AppException(AuthorMessage.WRONG_PASSWORD_LOCK_ACCOUNT.getCode(),
                        String.format(AuthorMessage.WRONG_PASSWORD_LOCK_ACCOUNT.getMessage(), maxNumPassWrong));

            } else {
                acc.setNumPwWrong(numPassWrong);
            }
            accountRepository.saveAndFlush(acc);
            throw new AppException(AuthorMessage.WRONG_PASSWORD.getCode(),
                    String.format(AuthorMessage.WRONG_PASSWORD.getMessage(), numPassWrong, maxNumPassWrong));
        }
        // check password expired
        if (acc.getPwExpDate() != null && acc.getPwExpDate().before(DateUtils.getNow())) {
            throw new AppException(AuthorMessage.EXPIRE_PASSWORD.getCode(),
                    AuthorMessage.EXPIRE_PASSWORD.getMessage());
        }
        acc.setNumPwWrong(0);
        accountRepository.save(acc);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        Account account = accountRepository.findByAccount(request.getAccount()).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại trong hệ thống"));
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AppException("ERR01","Mật khẩu không khớp");
        }
        account.setNumPwWrong(0);
        Calendar c = Calendar.getInstance();
        c.setTime(DateUtils.getNow());
        // get expire password config

        c.add(Calendar.DATE, ConfigDataCode.SYSTEM_EXPIRED_PASSWORD);
        account.setPwExpDate(c.getTime());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        accountRepository.save(account);
    }

    @Override
    public Boolean checkVerifyCode(String otp) {
        return accountRepository.existsByOtp(otp);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resendVerifyCode(String email) {
        if (email.contains("@")) {
            // case login by email
            email = email.split("@")[0];
        }
        Account account = accountRepository.findByAccount(email).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));
        String verifyCode = generateCode();
        account.setOtp(verifyCode);
        accountRepository.save(account);
        //Todo: Gui email code
        sendEmailService.sendEmailForAccount(account, EmailTemplate.CODE_REGISTER_PROVIDER);
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

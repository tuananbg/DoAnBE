package com.company_management.service.common.impl;

import com.company_management.common.AppConstants;
import com.company_management.common.Constants;
import com.company_management.common.enums.EmailTemplate;
import com.company_management.entity.*;
import com.company_management.exception.AppException;
import com.company_management.repository.AttendanceLeaveRepository;
import com.company_management.repository.AttendanceOTRepository;
import com.company_management.repository.EmployeeRepository;
import com.company_management.service.EmployeeService;
import com.company_management.service.common.SendEmailService;
import com.company_management.utils.DateUtils;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendEmailServiceImpl implements SendEmailService {
    private static final String UTF_8_ENCODING = "UTF-8";
    private final EmployeeRepository employeeRepository;
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender emailSender;
    private final AttendanceLeaveRepository attendanceLeaveRepository;
    private final AttendanceOTRepository attendanceOTRepository;
    private final EmployeeService employeeService;

    @Override
    public void sendEmailAttendance(String code, EmailTemplate emailTemplate, long id) {
        Employee employee = employeeRepository.findByCode(code).orElse(null);
        boolean hasSuccess = false;
        if (employee != null) {
            EmployeeInfo employeeInfo = employee.getEmployeeInfo();
            if (employeeInfo != null) {
                String email = employeeInfo.getEmail();
                if (email != null) {
                    try {
                        String processedContent = processTemplateContent(emailTemplate, id);
                        MimeMessage message = getMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
                        helper.setPriority(1);
                        helper.setSubject(emailTemplate.getSubject());
                        helper.setTo(email);
                        helper.setText(processedContent, true);
                        emailSender.send(message);
                        hasSuccess = true;
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        if (!hasSuccess){
            throw new AppException(AppConstants.EMAIL_SEND_CODE_FL1, AppConstants.EMAIL_SEND_MESS_FL1);
        }
    }

    @Override
    public void sendEmailForAccount(Account account,EmailTemplate emailTemplate) {
        Employee employee = account.getEmployee();
        boolean hasSuccess = false;
        if (employee != null) {
            EmployeeInfo employeeInfo = employee.getEmployeeInfo();
            if (employeeInfo != null) {
                String email = employeeInfo.getEmail();
                if (email != null) {
                    try {
                        String processedContent = processTemplateCreateAccountSuccess(account,emailTemplate);
                        MimeMessage message = getMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
                        helper.setPriority(1);
                        helper.setSubject(emailTemplate.getSubject());
                        helper.setTo(email);
                        helper.setText(processedContent, true);
                        emailSender.send(message);
                        hasSuccess = true;
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        if (!hasSuccess){
            throw new AppException(AppConstants.EMAIL_SEND_CODE_FL1, AppConstants.EMAIL_SEND_MESS_FL1);
        }
    }

    private String processTemplateContent(EmailTemplate emailTemplate, long id) {
        Context context = new Context();
        Map<String, Object> value;
        switch (emailTemplate) {
            case TEMPLATE_ATTENDANCE_LEAVE -> value = sendMailAttendanceLeave(id);
            case TEMPLATE_ATTENDANCE_OT -> value = sendMailAttendanceOT(id);
            default -> value = null;
        }
        context.setVariables(value);
        return templateEngine.process(emailTemplate.getTemplate(), context);
    }

    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }


    public Map<String, Object> sendMailAttendanceLeave(long id) {
        Map<String, Object> params = new HashMap<>();
        AttendanceLeave attendanceLeave = attendanceLeaveRepository.findById(id).orElse(null);
        if (attendanceLeave != null) {
            Employee employee = attendanceLeave.getEmployee();
            if (employee != null) {
                params.put("employeeName", employee.getFullName());
                params.put("employeeCode",employee.getCode());
            }
            else {
                params.put("employeeName",Constants.ADMIN_NAME);
                params.put("employeeCode", Constants.ADMIN);
            }
            Employee reviewer = attendanceLeave.getReviewer();
            if (reviewer != null) {
                params.put("reviewerName", reviewer.getFullName());
            }
            params.put("startDay", DateUtils.convertDateToStringAndFormat(attendanceLeave.getStartDay()) );
            params.put("endDay", DateUtils.convertDateToStringAndFormat(attendanceLeave.getEndDay()) );
            params.put("description", attendanceLeave.getDescription());
            params.put("leaveCategory", attendanceLeave.getLeaveCategory());
        }

        return params;
    }

    public Map<String,Object> sendMailAttendanceOT(long id){
        Map<String,Object> params = new HashMap<>();
        AttendanceOt attendanceOt = attendanceOTRepository.findById(id).orElse(null);
        if (attendanceOt != null){
            Employee employee =  attendanceOt.getEmployee();
            if (employee != null) {
                params.put("employeeName", employee.getFullName());
                params.put("employeeCode",employee.getCode());
            }
            else {
                params.put("employeeName",Constants.ADMIN_NAME);
                params.put("employeeCode", Constants.ADMIN);
            }
            Employee follower = attendanceOt.getEmployeeFollow();
            if (follower != null){
                params.put("followerName",follower.getFullName());
            }
            params.put("startDay", DateUtils.convertDateToStringAndFormat(attendanceOt.getStartDay()));
            params.put("startTime",DateUtils.getTime(attendanceOt.getStartTime()));
            params.put("endTime",DateUtils.getTime(attendanceOt.getEndTime()));
            params.put("descriptionOt",attendanceOt.getDescriptionOt());
        }
        return params;
    }

    public String processTemplateCreateAccountSuccess(Account account,EmailTemplate emailTemplate) {
        Context context = new Context();
        Map<String, Object> value;
        switch (emailTemplate) {
            case TEMPLATE_CREATE_ACCOUNT_SUCCESS -> value = processValueTemplateCreateAccountSuccess(account);
            case CODE_REGISTER_PROVIDER -> value = processValueTemplateResetPassword(account);
            default -> value = null;
        }

        context.setVariables(value);
        return templateEngine.process(emailTemplate.getTemplate(), context);
    }

    private Map<String, Object> processValueTemplateCreateAccountSuccess(Account account) {
        Map<String, Object> value = new HashMap<>();
        Employee employee = account.getEmployee();
        if (employee != null) {
            value.put("fullName", employee.getFullName());
            value.put("code", employee.getCode());
        }
        value.put("account",account.getAccount());
        value.put("password",AppConstants.DEFAULT_PASSWORD);
        return value;
    }

    private Map<String, Object> processValueTemplateResetPassword(Account account) {
        Map<String,Object> value = new HashMap<>();
        value.put("otp",account.getOtp());
        return value;

    }
}

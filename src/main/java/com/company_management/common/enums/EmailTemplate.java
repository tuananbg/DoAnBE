package com.company_management.common.enums;

import lombok.*;

@Getter
@NoArgsConstructor
public enum EmailTemplate {
    TEMPLATE_ATTENDANCE_LEAVE("[DTDI] Đơn đăng ký nghỉ phép", "template-attendance-leave.html"),
    TEMPLATE_ATTENDANCE_OT("[DTDI] Đơn đăng ký OT", "template-attendance-ot.html"),
    TEMPLATE_CREATE_ACCOUNT_SUCCESS("[DTDI] Thông báo tạo tài khoản thành công","create-account-success.html"),
    TEMPLATE_EMPLOYEE_CREATE_ACCOUNT("[DTDI] Thông báo tạo tài khoản HRM cho CBNV mới","employee-create-account.html"),
    CODE_REGISTER_PROVIDER("[DTDI] Thông báo mã xác nhận đổi mật khẩu","code-register-provider.html"),
    ;
    private String subjects;

    private String template;

    EmailTemplate(String subjects, String template) {
        this.subjects = subjects;
        this.template = template;
    }

    public String getSubject() {
        return this.subjects;
    }

}

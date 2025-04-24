package com.company_management.common.enums;

import lombok.*;

@Getter
@NoArgsConstructor
public enum EmailTemplate {
    TEMPLATE_ATTENDANCE_LEAVE("[DTDI] Đơn đăng ký nghỉ phép", "template-attendance-leave.html"),
    TEMPLATE_ATTENDANCE_OT("[DTDI] Đơn đăng ký OT", "template-attendance-ot.html");
    private String subjects;

    private String template;

    EmailTemplate(String subjects, String template) {
        this.subjects = subjects;
        this.template = template;
    }

    public String getSubject() {
        return this.subjects;
    }

    public String getTemplate() {
        return this.template;
    }

}

package com.company_management.common.enums;

public enum AuthorMessage {

    WRONG_PASSWORD("100", "Tên đăng nhập hoặc mật khẩu không hợp lệ."),
    EXPIRE_PASSWORD("101", "Mật khẩu hết hạn. Vui lòng liên hệ Trung tâm Công nghệ thông tin để được hỗ trợ."),
    ACCOUNT_LOCK("102", "Không thể đăng nhập do tài khoản đã bị khoá. Vui lòng liên hệ với phòng Nhân Sự và Đào tạo để được hỗ trợ và hướng dẫn."),
    ACCOUNT_RETIRED("103", "Cán bộ nhân viên đã nghỉ việc. Vui lòng liên hệ Trung tâm Công nghệ thông tin để được hỗ trợ."),
    ACCOUNT_NOT_FOUND("104", "Tên đăng nhập hoặc mật khẩu không hợp lệ."),
    WRONG_CURENT_PASSWORD("105", "Mật khẩu hiện tại không đúng."),
    DUPLICATE_WITH_CURRENT_PASSWORD("107","Mật khẩu mới trùng với mật khẩu cũ. Vui lòng liên hệ Trung tâm Công nghệ thông tin để được hỗ trợ."),
    CONFIRM_PASSWORD_NOT_MATCH("108","Confirm Password không chính xác. Vui lòng liên hệ Trung tâm Công nghệ thông tin để được hỗ trợ"),
    WRONG_PASSWORD_LOCK_ACCOUNT("109", "Tài khoản bị khoá do nhập sai mật khẩu quá %s lần. Vui lòng liên hệ Trung tâm Công nghệ thông tin để được hỗ trợ.");

    private String code;

    private String message;

    AuthorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}

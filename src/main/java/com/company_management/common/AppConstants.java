package com.company_management.common;

public class AppConstants {
    private AppConstants() {
    }

    /* common controller */
    public static final String STATUS_200 = "200";
    public static final String MESSAGE_200 = "Request processed successfully";
    public static final String STATUS_201 = "201";
    public static final String MESSAGE_201 = "Created successfully";
    public static final String STATUS_417 = "417";
    public static final String MESSAGE_417_UPDATE =
            "Update operation failed. Please try again or contact Dev team";
    public static final String MESSAGE_417_DELETE =
            "Delete operation failed. Please try again or contact Dev team";
    public static final String STATUS_400 = "400";
    public static final String STATUS_500 = "500";

    public static final String EMPLOYEE_201 = "Employee created successfully";
    ///  CBNV
    public static final String EMPLOYEE_CODE_001 = "EMP001";
    public static final String EMPLOYEE_MESS_001 = "Không tìm thấy nhân viên này!";
    public static final String EMPLOYEE_CODE_002 = "EMP002";
    public static final String EMPLOYEE_MESS_002 = "Mã nhân viên đã tồn tại";
    public static final String DEPARTMENT_CODE_001 = "DEP001";
    public static final String DEPARTMENT_MESS_001 = "Không tìm thấy phòng ban này!";
    public static final String DEPARTMENT_CODE_002 = "DEP002";
    public static final String DEPARTMENT_MESS_002 = "Phòng ban không hoạt động!";
    public static final String POSITION_CODE_001 = "POS001";
    public static final String POSITION_MESS_001 = "Không tìm thấy chức vụ này!";
    public static final String POSITION_CODE_002 = "POS002";
    public static final String POSITION_MESS_002 = "Chức vụ không hoạt động";

    public static final String UPLOAD_FILE_IMAGE_CODE_001 = "FILE001";
    public static final String UPLOAD_FILE_IMAGE_MESS_001 = "Tên tệp tin không hợp lệ!";

    public static final String UPLOAD_FILE_IMAGE_CODE_002 = "FILE002";
    public static final String UPLOAD_FILE_IMAGE_MESS_002 = "Tệp tin là rỗng";

    public static final String UPLOAD_FILE_IMAGE_CODE_003 = "FILE003";
    public static final String UPLOAD_FILE_IMAGE_MESS_003 = "Lỗi xảy ra khi xử lý file ảnh!";
    public static final String EXPORT_FILE_EXCEL_CODE_001 = "EXCEL001";
    public static final String EXPORT_FILE_EXCEL_MESS_001 = "Xuất file excel bị lỗi!";


}

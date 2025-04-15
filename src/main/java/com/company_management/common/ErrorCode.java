package com.company_management.common;

public class ErrorCode {

    public static final String OK = "200";
    public static final ObjectError SELECT_FAIL = new ObjectError("EX006", Constants.SELECT_FAIL);
    public static final ObjectError DELETED_FAIL = new ObjectError("EX009", Constants.DELETED_FAIL);


    public static final ObjectError CREATED_OK = new ObjectError("OK", Constants.CREATED_OK);
    public static final ObjectError UPDATED_OK = new ObjectError("OK", Constants.UPDATED_OK);
    public static final ObjectError DELETED_OK = new ObjectError("OK", Constants.DELETED_OK);

}

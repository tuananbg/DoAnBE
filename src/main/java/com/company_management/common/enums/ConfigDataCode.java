package com.company_management.common.enums;

import lombok.Getter;

@Getter
public class ConfigDataCode {
    // System config
    public static final String SYSTEM_GROUP_TYPE = "SYSTEM";
    public static final int SYSTEM_EXPIRED_PASSWORD = 60;
    public static final int SYSTEM_NUM_PASSWORD_WRONG = 5;
    public static final String SYSTEM_ADMIN_PASSWORD = "$2a$10$xJ.tnbOZkOPTVb4ObJJS5evCDAc/h/5xoqfpLwMEXtyOjZUT7NF7W";

    public static final String ADMIN="ADMIN";
}

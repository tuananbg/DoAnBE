package com.company_management.common.enums;

import lombok.Getter;

@Getter
public class ConfigDataCode {
    public static final String LDAP_GROUP_TYPE = "LDAP";
    public static final String LDAP_PATH = "LDAP_PATH";
    public static final String LDAP_DOMAIN = "LDAP_DOMAIN";
    public static final String LDAP_USER = "LDAP_USER";
    public static final String LDAP_PASSWORD = "LDAP_PASSWORD";
    // System config
    public static final String SYSTEM_GROUP_TYPE = "SYSTEM";
    public static final int SYSTEM_EXPIRED_PASSWORD = 60;
    public static final int SYSTEM_NUM_PASSWORD_WRONG = 5;
    public static final String SYSTEM_ADMIN_PASSWORD = "$2a$10$xJ.tnbOZkOPTVb4ObJJS5evCDAc/h/5xoqfpLwMEXtyOjZUT7NF7W";

}

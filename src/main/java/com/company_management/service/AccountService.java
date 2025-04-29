package com.company_management.service;

import com.company_management.common.enums.AccountStatusEnum;
import com.company_management.dto.au.ChangePasswordRequest;
import com.company_management.dto.au.EmployeeAccountRequestDTO;
import com.company_management.dto.au.RequestAddRoleDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.pa.UserCustomEmployeeRequest;
import com.company_management.dto.response.*;
import com.company_management.dto.response.au.ResponseAccountListDTO;
import com.company_management.dto.response.au.ResponseLoginDTO;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface AccountService {
    void createAccount(EmployeeAccountRequestDTO dto) throws UnsupportedEncodingException;

    void updateStatusAccount(Long id,Integer status);

    void lockEmployee(String employeeCode);

    void unlockEmployee(String employeeCode);

    List<String> addEmployeeRole(RequestAddRoleDTO request);

    Boolean removeEmployeeRole(String employeeCode, String RoleCode);

    ResponsePage<ResponseAccountListDTO> getList(AccountStatusEnum status, String keyword, RequestPage page);

    ResponseAccountRole findAccountRole(Long id);

    void editUserCustom(UserCustomEmployeeRequest userCustomEmployeeRequest);

    Boolean changePassword(ChangePasswordRequest request);

    Boolean checkVerifyCode(String otp);

    void resendVerifyCode(String account) ;
}

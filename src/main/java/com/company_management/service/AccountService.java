package com.company_management.service;

import com.company_management.common.enums.AccountStatusEnum;
import com.company_management.dto.au.EmployeeAccountRequestDTO;
import com.company_management.dto.au.RequestAddRoleDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.response.*;
import com.company_management.dto.response.au.ResponseAccountListDTO;

import java.io.UnsupportedEncodingException;

public interface AccountService {
    void createAccount(EmployeeAccountRequestDTO dto) throws UnsupportedEncodingException;

    void lockEmployee(String employeeCode);

    void unlockEmployee(String employeeCode);

    void addEmployeeRole(RequestAddRoleDTO request);

    ResponsePage<ResponseAccountListDTO> getList(AccountStatusEnum status, String keyword, RequestPage page);

    ResponseAccountRole findAccountRole(Long id);

}

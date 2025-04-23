package com.company_management.service;

import com.company_management.common.enums.AccountStatusEnum;
import com.company_management.dto.au.EmployeeAccountRequestDTO;
import com.company_management.dto.au.RequestAddRoleDTO;
import com.company_management.dto.common.BasicResponse;
import com.company_management.dto.common.PageResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.pa.UserCustomEmployeeRequest;
import com.company_management.dto.request.pa.UserDetailRequest;
import com.company_management.dto.request.pa.UserSearchRequest;
import com.company_management.dto.response.*;
import com.company_management.dto.response.au.AccountDetailResponse;
import com.company_management.dto.response.au.ResponseAccountListDTO;
import com.company_management.entity.Account;
import com.company_management.entity.Employee;
import org.springframework.data.domain.Pageable;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface AccountService {
    String createAccount(EmployeeAccountRequestDTO dto) throws UnsupportedEncodingException;

    void updateStatusAccount(Long id,Integer status);

    Boolean lockEmployee(String employeeCode);

    Boolean unlockEmployee(String employeeCode);

    List<String> addEmployeeRole(RequestAddRoleDTO request);

    Boolean removeEmployeeRole(String employeeCode, String RoleCode);

    ResponsePage<ResponseAccountListDTO> getList(AccountStatusEnum status, String keyword, RequestPage page);

    ResponseAccountRole findAccountRole(Long id);

    void editUserCustom(UserCustomEmployeeRequest userCustomEmployeeRequest);
}

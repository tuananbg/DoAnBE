package com.company_management.service;

import com.company_management.dto.common.BasicResponse;
import com.company_management.dto.common.PageResponse;
import com.company_management.dto.request.pa.UserCustomEmployeeRequest;
import com.company_management.dto.request.pa.UserDetailRequest;
import com.company_management.dto.request.pa.UserSearchRequest;
import com.company_management.dto.response.*;
import com.company_management.dto.response.au.AccountDetailResponse;
import com.company_management.dto.response.au.AccountSearchResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {
    PageResponse<UserSearchResponse> searchUser(UserSearchRequest request, Pageable pageable);

    UserSearchResponse findUserDetailById(Long id);

    BasicResponse createUserDetail(UserDetailRequest request);

    AccountDetailResponse findAccountDetail(Long id);

    PageResponse<AccountSearchResponse> searchAccount(Pageable pageable);

    ResponseAccountRole findAccountRole(Long id);

    void editUserCustom(UserCustomEmployeeRequest userCustomEmployeeRequest);
}

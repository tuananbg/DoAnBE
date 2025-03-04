package com.company_management.service;

import com.company_management.dto.request.AccountSearchRequest;
import com.company_management.dto.request.UserCustomEmployeeRequest;
import com.company_management.dto.request.UserDetailRequest;
import com.company_management.dto.request.UserSearchRequest;
import com.company_management.dto.response.*;
import org.springframework.data.domain.Pageable;

public interface UserService {
    PageResponse<UserSearchResponse> searchUser(UserSearchRequest request, Pageable pageable);

    UserSearchResponse findUserDetailById(Long id);

    BasicResponse createUserDetail(UserDetailRequest request);

    AccountDetailResponse findAccountDetail(Long id);

    PageResponse<AccountSearchResponse> searchAccount( Pageable pageable);

    ResponseAccountRole findAccountRole(Long id);

    void editUserCustom(UserCustomEmployeeRequest userCustomEmployeeRequest);
}

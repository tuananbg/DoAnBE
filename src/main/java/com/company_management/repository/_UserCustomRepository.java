package com.company_management.repository;

import com.company_management.dto.request.AccountSearchRequest;
import com.company_management.dto.request.UserSearchRequest;
import com.company_management.dto.response.AccountDetailResponse;
import com.company_management.dto.response.AccountSearchResponse;
import com.company_management.dto.response.PageResponse;
import com.company_management.dto.response.UserSearchResponse;
import org.springframework.data.domain.Pageable;


public interface _UserCustomRepository {
    PageResponse<UserSearchResponse> searchAllUser(UserSearchRequest request, Pageable pageable);

    AccountDetailResponse findAccountDetail(Long id);

    PageResponse<AccountSearchResponse> searchAccount( Pageable pageable);
}

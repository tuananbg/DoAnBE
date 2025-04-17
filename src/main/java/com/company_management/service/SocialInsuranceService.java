package com.company_management.service;

import com.company_management.dto.SocialInsuranceDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.request.RequestSocialInsuranceDTO;
import com.company_management.dto.response.ResponseSocialInsuranceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SocialInsuranceService {
    Page<ResponseSocialInsuranceDTO> getListEmployee(String employeeCode, RequestPage pageable);

    SocialInsuranceDTO detail(Long id);

    void update(SocialInsuranceDTO socialInsuranceDTO);

    void create(RequestSocialInsuranceDTO request);

    void deleteByIds(Long id);

}

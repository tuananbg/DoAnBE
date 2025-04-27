package com.company_management.service;

import com.company_management.dto.SocialInsuranceDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.request.pa.RequestSocialInsuranceDTO;
import com.company_management.dto.response.pa.ResponseSocialInsuranceDTO;
import org.springframework.data.domain.Page;

public interface SocialInsuranceService {
    Page<ResponseSocialInsuranceDTO> getListEmployee(String employeeCode, RequestPage pageable);

//    SocialInsuranceDTO detail(Long id);

    void update(RequestSocialInsuranceDTO request);

    void create(RequestSocialInsuranceDTO request);

    void deleteByIds(Long id);

}

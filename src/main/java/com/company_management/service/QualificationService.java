package com.company_management.service;

import com.company_management.dto.QualificationDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.RequestQualificationDTO;
import com.company_management.dto.response.ResponseQualificationDTO;
import com.company_management.dto.response.ResponseQualificationEmployeeDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface QualificationService {
//    Page<QualificationDTO> search(Long userDetailId, Pageable pageable);

    ResponseQualificationDTO detail(Long id);

    void update(QualificationDTO qualificationDTO);

    void create(RequestQualificationDTO request);

    void deleteByIds(Long id);

    ResponsePage<ResponseQualificationEmployeeDetailDTO> getDetailEmployees(String employeeCode,RequestPage page);
}

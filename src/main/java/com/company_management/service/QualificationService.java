package com.company_management.service;

import com.company_management.dto.QualificationDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.pa.RequestQualificationDTO;
import com.company_management.dto.response.pa.ResponseQualificationDTO;
import com.company_management.dto.response.pa.ResponseQualificationEmployeeDetailDTO;

public interface QualificationService {
//    Page<QualificationDTO> search(Long userDetailId, Pageable pageable);

    ResponseQualificationDTO detail(Long id);

    void update(QualificationDTO qualificationDTO);

    void create(RequestQualificationDTO request);

    void deleteByIds(Long id);

    ResponsePage<ResponseQualificationEmployeeDetailDTO> getDetailEmployees(String employeeCode,RequestPage page);
}

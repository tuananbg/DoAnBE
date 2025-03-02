package com.company_management.service;

import com.company_management.dto.QualificationDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.response.ResponseQualificationEmployeeDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface QualificationService {
//    Page<QualificationDTO> search(Long userDetailId, Pageable pageable);

    QualificationDTO detail(Long id);

    void update(QualificationDTO qualificationDTO);

    void addQualification(QualificationDTO qualificationDTO);

    void deleteByIds(Long id);

    List<ResponseQualificationEmployeeDetailDTO> getDetailEmployees(Long userDetailId);
}

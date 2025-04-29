package com.company_management.service.common;

import com.company_management.common.enums.ContractStatusEnum;
import com.company_management.common.enums.ObjectStatus;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface JasperReportService {
    byte[] employeeFullInformation();

    byte[] contractStatus(ContractStatusEnum status);

    byte[] department(ObjectStatus status);

    ResponseEntity<Resource> baseDownload(byte[] bytes, String fileName);
}

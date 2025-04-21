package com.company_management.service.common;

import com.company_management.common.enums.ReportType;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface JasperReportService {
    byte[] employeeFullInformation();

    ResponseEntity<Resource> baseDownload(byte[] bytes, String fileName);
}

package com.company_management.service.common;

import com.company_management.common.enums.*;
import com.company_management.entity.AttendanceOt;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface JasperReportService {
    byte[] employeeFullInformation(EmploymentStatus status);

    byte[] contractStatus(ContractStatusEnum status);

    byte[] positionStatus(ObjectStatus status);

    byte[] attendanceLeave(AttendanceLeaveStatus status);

    byte[] attendanceOt(AttendanceLeaveStatus status);

    byte[] taskStatus(TaskStatusEnum status);

    byte[] timeSheetEmployeeExcessReportData(String monthCode);

    ResponseEntity<Resource> baseDownload(byte[] bytes, String fileName);
}

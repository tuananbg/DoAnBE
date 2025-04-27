package com.company_management.service;

import com.company_management.dto.AttendanceDTO;
import com.company_management.dto.request.attendace.RequestAttendanceDTO;
import com.company_management.dto.request.pa.SearchAttendanceRequest;
import com.company_management.dto.response.attendance.AttendanceResponse;
import com.company_management.dto.common.DataPage;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.util.Date;

public interface AttendanceService {

    DataPage<AttendanceResponse> search(SearchAttendanceRequest searchAttendanceRequest, Pageable pageable);

    void create(RequestAttendanceDTO requestAttendanceDTO);

    void createOrUpdate(AttendanceDTO attendanceDTO);

    ByteArrayInputStream exportExcel(SearchAttendanceRequest searchAttendanceRequest, Pageable pageable);

    Long detailAttendanceId(String employeeCode);
}

package com.company_management.service;

import com.company_management.dto.AttendanceDTO;
import com.company_management.dto.request.SearchAttendanceRequest;
import com.company_management.dto.response.AttendanceResponse;
import com.company_management.dto.response.DataPage;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;

public interface AttendanceService {

    DataPage<AttendanceResponse> search(SearchAttendanceRequest searchAttendanceRequest, Pageable pageable);

    void createOrUpdate(AttendanceDTO attendanceDTO);

    ByteArrayInputStream exportExcel(SearchAttendanceRequest searchAttendanceRequest, Pageable pageable);

    Long detailAttendanceId(AttendanceDTO attendanceDTO);
}

package com.company_management.service;

import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.attendace.RequestAttendanceDTO;
import com.company_management.dto.request.pa.SearchAttendanceRequest;
import com.company_management.dto.response.attendance.ResponseAttendanceDTO;
import com.company_management.dto.common.DataPage;
import com.company_management.dto.response.attendance.ResponseAttendanceStatusDTO;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;

public interface AttendanceService {

    ResponsePage<ResponseAttendanceDTO> getList(RequestPage page, SearchAttendanceRequest search);

    void create(RequestAttendanceDTO requestAttendanceDTO);

    void update( RequestAttendanceDTO request);

    ByteArrayInputStream exportExcel(SearchAttendanceRequest searchAttendanceRequest, Pageable pageable);

    ResponseAttendanceStatusDTO getAttendanceId(String employeeCode);
}

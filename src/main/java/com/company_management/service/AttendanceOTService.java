package com.company_management.service;

import com.company_management.dto.AttendanceOTDTO;
import com.company_management.dto.request.SearchAttendanceOTRequest;
import com.company_management.dto.response.DataPage;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;

public interface AttendanceOTService {

    DataPage<AttendanceOTDTO> search(SearchAttendanceOTRequest searchAttendanceOTRequest, Pageable pageable);

    AttendanceOTDTO detailOT(Long id);

    void createOrUpdate(AttendanceOTDTO attendanceOTDTO);

    void deleteOT(Long id);

    ByteArrayInputStream exportExcel(SearchAttendanceOTRequest searchAttendanceOTRequest, Pageable pageable);


}

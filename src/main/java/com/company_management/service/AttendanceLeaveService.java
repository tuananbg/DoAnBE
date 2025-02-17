package com.company_management.service;

import com.company_management.dto.AttendanceLeaveDTO;
import com.company_management.dto.request.SearchLeaveRequest;
import com.company_management.dto.response.DataPage;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;

public interface AttendanceLeaveService {

    DataPage<AttendanceLeaveDTO> search(SearchLeaveRequest searchLeaveRequest, Pageable pageable);

    AttendanceLeaveDTO detailLeave(Long id);

    void createOrUpdate(AttendanceLeaveDTO leaveDTO);

    void deleteLeave(Long id);

    ByteArrayInputStream exportExcel(SearchLeaveRequest searchLeaveRequest, Pageable pageable);


}

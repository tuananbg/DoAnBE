package com.company_management.service;

import com.company_management.common.enums.AttendanceLeaveStatus;
import com.company_management.dto.AttendanceLeaveDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.attendace.SearchLeaveRequest;
import com.company_management.dto.common.DataPage;
import com.company_management.dto.response.attendance.ResponseAttendanceLeaveDTO;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;

public interface AttendanceLeaveService {

    ResponsePage<ResponseAttendanceLeaveDTO> search(AttendanceLeaveStatus status, String keyword, RequestPage page);

    AttendanceLeaveDTO detailLeave(Long id);

    void createOrUpdate(AttendanceLeaveDTO leaveDTO);

    void deleteLeave(Long id);

    ByteArrayInputStream exportExcel(SearchLeaveRequest searchLeaveRequest, Pageable pageable);


}

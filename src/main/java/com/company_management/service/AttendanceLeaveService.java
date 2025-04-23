package com.company_management.service;

import com.company_management.common.enums.AttendanceLeaveStatus;
import com.company_management.common.enums.TableTabType;
import com.company_management.dto.AttendanceLeaveDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.attendace.RequestAttendanceLeaveDTO;
import com.company_management.dto.request.attendace.SearchLeaveRequest;
import com.company_management.dto.common.DataPage;
import com.company_management.dto.response.attendance.ResponseAttendanceLeaveDTO;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;

public interface AttendanceLeaveService {

    ResponsePage<ResponseAttendanceLeaveDTO> search(TableTabType status, String keyword, RequestPage page);

    AttendanceLeaveDTO detailLeave(Long id);

    void create(RequestAttendanceLeaveDTO request);

    void deleteLeave(Long id);

    ByteArrayInputStream exportExcel(SearchLeaveRequest searchLeaveRequest, Pageable pageable);


}

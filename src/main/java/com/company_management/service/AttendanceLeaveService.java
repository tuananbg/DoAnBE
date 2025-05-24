package com.company_management.service;

import com.company_management.common.enums.TableTabType;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.attendace.RequestAttendanceLeaveDTO;
import com.company_management.dto.request.attendace.RequestUpdateAttendanceLeaveDTO;
import com.company_management.dto.request.attendace.SearchLeaveRequest;
import com.company_management.dto.response.attendance.ResponseAttendanceLeaveDTO;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;

public interface AttendanceLeaveService {

    ResponsePage<ResponseAttendanceLeaveDTO> search(TableTabType status, String keyword, RequestPage page);

    void create(RequestAttendanceLeaveDTO request);

    void update(RequestUpdateAttendanceLeaveDTO request);


    void complete(RequestUpdateAttendanceLeaveDTO request);
}

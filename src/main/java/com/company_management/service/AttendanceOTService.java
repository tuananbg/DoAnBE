package com.company_management.service;

import com.company_management.common.enums.TableTabType;
import com.company_management.dto.AttendanceOTDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.attendace.RequestAttendanceOTDTO;
import com.company_management.dto.request.attendace.RequestUpdateAttendanceOTDTO;
import com.company_management.dto.request.pa.SearchAttendanceOTRequest;
import com.company_management.dto.common.DataPage;
import com.company_management.dto.response.attendance.ResponseAttendanceOTDTO;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;

public interface AttendanceOTService {

    ResponsePage<ResponseAttendanceOTDTO> getList(TableTabType status, String keyword, RequestPage page);

    AttendanceOTDTO detailOT(Long id);

    void create(RequestAttendanceOTDTO request);

    void update(RequestUpdateAttendanceOTDTO request);

    void complete(RequestUpdateAttendanceOTDTO request);

    void deleteOT(Long id);

    ByteArrayInputStream exportExcel(SearchAttendanceOTRequest searchAttendanceOTRequest, Pageable pageable);


}

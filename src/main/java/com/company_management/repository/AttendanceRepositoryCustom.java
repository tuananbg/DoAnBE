package com.company_management.repository;

import com.company_management.dto.request.SearchAttendanceRequest;
import com.company_management.dto.response.AttendanceResponse;
import com.company_management.dto.response.DataPage;
import org.springframework.data.domain.Pageable;

public interface AttendanceRepositoryCustom {
    DataPage<AttendanceResponse> search(SearchAttendanceRequest searchAttendanceRequest, Pageable pageable);

}

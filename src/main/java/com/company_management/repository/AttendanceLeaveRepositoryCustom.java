package com.company_management.repository;

import com.company_management.dto.AttendanceLeaveDTO;
import com.company_management.dto.request.SearchLeaveRequest;
import com.company_management.dto.response.DataPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AttendanceLeaveRepositoryCustom {
    DataPage<AttendanceLeaveDTO> search(SearchLeaveRequest searchLeaveRequest, Pageable pageable);

    List<AttendanceLeaveDTO> searchExport(SearchLeaveRequest searchLeaveRequest, Pageable pageable);

}

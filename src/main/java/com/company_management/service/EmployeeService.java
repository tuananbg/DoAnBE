package com.company_management.service;

import com.company_management.common.enums.EmploymentStatus;
import com.company_management.dto.UserDetailDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.SearchEmployeeRequest;
import com.company_management.dto.request.employee.RequestEmployeeDetailDTO;
import com.company_management.dto.response.DataPage;
import com.company_management.dto.response.ExportPdfEmployeeResponse;
import com.company_management.dto.response.TotalEmployeeDTO;
import com.company_management.dto.response.employee.ResponseEmployeeDetailDTO;
import com.company_management.dto.response.employee.ResponseListEmployeeDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface EmployeeService {

    ResponsePage<ResponseListEmployeeDTO> findAllByKeywordAndStatus(String keyword, RequestPage page);

    ResponseEmployeeDetailDTO detailEmployee(Long id);

    void createEmployee(MultipartFile avatarFile, RequestEmployeeDetailDTO request) throws IOException;

    void updateEmployee(MultipartFile avatarFile, UserDetailDTO userDetailDTO) throws IOException;

    void deleteEmployee(Long id);

    ByteArrayInputStream exportExcel(SearchEmployeeRequest searchEmployeeRequest, Pageable pageable);

//    void updateEmployeeStatus();

    ExportPdfEmployeeResponse exportPdf(Long userDetailId);

    void lockEmployee(Long id);

    TotalEmployeeDTO totalEmployee(Long id);
}

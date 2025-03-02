package com.company_management.service;

import com.company_management.dto.UserDetailDTO;
import com.company_management.dto.request.SearchEmployeeRequest;
import com.company_management.dto.request.employee.RequestEmployeeDetailDTO;
import com.company_management.dto.response.DataPage;
import com.company_management.dto.response.ExportPdfEmployeeResponse;
import com.company_management.dto.response.TotalEmployeeDTO;
import com.company_management.dto.response.employee.ResponseEmployeeDetailDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface EmployeeService {

    DataPage<UserDetailDTO> search(SearchEmployeeRequest searchEmployeeRequest, Pageable pageable);

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

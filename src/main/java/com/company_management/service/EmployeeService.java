package com.company_management.service;

import com.company_management.common.enums.EmploymentStatus;
import com.company_management.dto.UserDetailDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.pa.SearchEmployeeRequest;
import com.company_management.dto.request.pa.employee.RequestEmployeeDetailDTO;
import com.company_management.dto.response.ExportPdfEmployeeResponse;
import com.company_management.dto.response.TotalEmployeeDTO;
import com.company_management.dto.response.pa.employee.ResponseEmployeeDetailDTO;
import com.company_management.dto.response.pa.employee.ResponseEmployeeSelectDTO;
import com.company_management.dto.response.pa.employee.ResponseListEmployeeDTO;
import com.company_management.entity.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public interface EmployeeService {

    ResponsePage<ResponseListEmployeeDTO> findAllByKeywordAndStatus(String keyword, EmploymentStatus status, RequestPage page);

    ResponseEmployeeDetailDTO detailEmployeeCode(String code);

    void createEmployee(MultipartFile avatarFile, RequestEmployeeDetailDTO request) throws IOException;

    void updateEmployee(MultipartFile avatarFile, RequestEmployeeDetailDTO request) throws IOException;

    void lockEmployee(Long id);

    TotalEmployeeDTO totalEmployee(String code);

    List<ResponseEmployeeSelectDTO> selectEmployee();

    Employee getEmployee(String code);
}

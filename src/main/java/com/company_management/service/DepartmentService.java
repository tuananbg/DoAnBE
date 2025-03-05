package com.company_management.service;

import com.company_management.dto.DepartmentDTO;
import com.company_management.dto.request.SearchDepartmentRequest;
import com.company_management.dto.response.ResponseDepartmentDTO;
import com.company_management.dto.response.ResponseTotalDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {

    Page<DepartmentDTO> findAllPage(SearchDepartmentRequest searchDepartmentRequest, Pageable pageable);

    void addDepartment(DepartmentDTO departmentDTO);

    void editDepartment(DepartmentDTO departmentDTO);

    void deleteDepartment(Long id);

    ResponseDepartmentDTO detailDepartment(Long id);

    List<ResponseTotalDTO> totalDepartment();
}

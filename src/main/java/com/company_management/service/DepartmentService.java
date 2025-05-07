package com.company_management.service;

import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.DepartmentDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.response.pa.ResponseDepartmentDTO;
import com.company_management.dto.response.ResponseTotalDTO;

import java.util.List;

public interface DepartmentService {

    ResponsePage<ResponseDepartmentDTO> findAllPage(ObjectStatus status, String keyword, RequestPage pageable);

    void addDepartment(DepartmentDTO departmentDTO);

    void editDepartment(DepartmentDTO departmentDTO);

    void lock(String departmentCode);

    void unlock(String departmentCode);

    ResponseDepartmentDTO detailDepartment(Long id);

    List<ResponseTotalDTO> totalDepartment();

    List<ResponseDepartmentDTO> getListAllDepartment();
}

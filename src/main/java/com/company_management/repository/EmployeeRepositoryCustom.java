package com.company_management.repository;

import com.company_management.dto.UserDetailDTO;

import java.util.List;

public interface EmployeeRepositoryCustom {

    void insertBatch(List<UserDetailDTO> employeeExcelLst, String staffCode);
}

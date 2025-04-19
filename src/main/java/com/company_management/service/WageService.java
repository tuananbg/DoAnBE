package com.company_management.service;

import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.ResponseWageEmployeeDetailDTO;
import com.company_management.dto.UserDetailWageDTO;
import com.company_management.dto.WageDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.common.DataPage;
import com.company_management.dto.response.ResponseWageListDTO;
import com.company_management.dto.response.WageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface WageService {

    DataPage<WageDTO> searchForEmployee(WageDTO wageDTO, Pageable pageable);

    ResponsePage<ResponseWageListDTO> getList(ObjectStatus status,String keyword, RequestPage page);

    WageResponse detail(Long id);

    void update(MultipartFile file, WageDTO wageDTO);

    void updateForEmployee(UserDetailWageDTO userDetailWageDTO);

    void add(MultipartFile file, WageDTO contractDTO);

    void addForEmployee(UserDetailWageDTO userDetailWageDTO);

    void deleteByIds(Long id);

    void deleteForEmployeeByIds(Long id);

    ResponsePage<ResponseWageEmployeeDetailDTO> getEmployeeWageDetails(String employeeCode,RequestPage requestPage);
}

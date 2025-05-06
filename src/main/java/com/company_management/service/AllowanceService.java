package com.company_management.service;

import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.ResponseWageEmployeeDetailDTO;
import com.company_management.dto.UserDetailWageDTO;
import com.company_management.dto.WageDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.common.DataPage;
import com.company_management.dto.request.pa.RequestAllowanceCreateDTO;
import com.company_management.dto.response.WageResponse;
import com.company_management.dto.response.pa.ResponseAllowanceListDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AllowanceService {

    DataPage<WageDTO> searchForEmployee(WageDTO wageDTO, Pageable pageable);

    ResponsePage<ResponseAllowanceListDTO> getList(ObjectStatus status, String keyword, RequestPage page);

    WageResponse detail(Long id);

    void update(MultipartFile file, RequestAllowanceCreateDTO request);

    void updateForEmployee(UserDetailWageDTO userDetailWageDTO);

    void create(MultipartFile file, RequestAllowanceCreateDTO request);

    void addForEmployee(UserDetailWageDTO userDetailWageDTO);

    void lock(String allowanceCode);

    void deleteForEmployeeByIds(Long id);

    ResponsePage<ResponseWageEmployeeDetailDTO> getEmployeeWageDetails(String employeeCode,RequestPage requestPage);
}

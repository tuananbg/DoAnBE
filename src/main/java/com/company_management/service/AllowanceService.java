package com.company_management.service;

import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.response.pa.ResponseAllowanceEmployeeDetailDTO;
import com.company_management.dto.UserDetailWageDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.pa.RequestAllowanceCreateDTO;
import com.company_management.dto.request.pa.RequestEmployeeAllowanceDTO;
import com.company_management.dto.response.pa.ResponseAllowanceListDTO;
import com.company_management.dto.response.pa.ResponseSelectAllowanceDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AllowanceService {

    ResponsePage<ResponseAllowanceListDTO> getList(ObjectStatus status, String keyword, RequestPage page);

    List<ResponseSelectAllowanceDTO> select( );

    void update(MultipartFile file, RequestAllowanceCreateDTO request);

    void updateForEmployee(UserDetailWageDTO userDetailWageDTO);

    void create(MultipartFile file, RequestAllowanceCreateDTO request);

    void addForEmployee(RequestEmployeeAllowanceDTO request);

    void lock(String allowanceCode);

    void unlock(String allowanceCode);

    void deleteForEmployeeByIds(String employeeCode, String allowanceCode);

    ResponsePage<ResponseAllowanceEmployeeDetailDTO> getEmployeeWageDetails(String employeeCode, RequestPage requestPage);
}

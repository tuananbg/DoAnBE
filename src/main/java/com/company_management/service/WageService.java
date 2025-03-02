package com.company_management.service;

import com.company_management.dto.ResponseWageEmployeeDetailDTO;
import com.company_management.dto.UserDetailWageDTO;
import com.company_management.dto.WageDTO;
import com.company_management.dto.response.DataPage;
import com.company_management.dto.response.WageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WageService {

    DataPage<WageDTO> searchForEmployee(WageDTO wageDTO, Pageable pageable);

    DataPage<WageDTO> search(WageDTO wageDTO, Pageable pageable);

    WageResponse detail(Long id);

    void update(MultipartFile file, WageDTO wageDTO);

    void updateForEmployee(UserDetailWageDTO userDetailWageDTO);

    void add(MultipartFile file, WageDTO contractDTO);

    void addForEmployee(UserDetailWageDTO userDetailWageDTO);

    void deleteByIds(Long id);

    void deleteForEmployeeByIds(Long id);

    List<ResponseWageEmployeeDetailDTO> getEmployeeWageDetails(Long id);
}

package com.company_management.service;

import com.company_management.dto.ContractDTO;
import com.company_management.dto.UserDetailContractDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.response.ResponseEmployeeContractsDetail;
import com.company_management.dto.response.DataPage;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeContractService {
    DataPage<ContractDTO> searchForEmployee(ContractDTO contractDTO, Pageable page);

    DataPage<ContractDTO> search(ContractDTO contractDTO, Pageable pageable);

    ResponseEmployeeContractsDetail detail(Long id);

    void update(MultipartFile file, ContractDTO contractDTO);

    void updateForEmployee(UserDetailContractDTO userDetailContractDTO);

    void add(MultipartFile file, ContractDTO contractDTO);

    void addForEmployee(UserDetailContractDTO userDetailContractDTO);

}

package com.company_management.service;

import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.ContractDTO;
import com.company_management.dto.UserDetailContractDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.RequestEmployeeContractDTO;
import com.company_management.dto.response.ResponseContractListDTO;
import com.company_management.dto.response.ResponseEmployeeContractsDetail;
import com.company_management.dto.response.DataPage;
import com.company_management.dto.response.ResponseTotalDTO;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeContractService {
    DataPage<ContractDTO> searchForEmployee(ContractDTO contractDTO, Pageable page);

    ResponsePage<ResponseContractListDTO> getList(ObjectStatus status,String keyword, RequestPage page);

    ResponseEmployeeContractsDetail detail(Long id);

    void update(MultipartFile file, ContractDTO contractDTO);

    void updateForEmployee(UserDetailContractDTO userDetailContractDTO);

    void create(MultipartFile file, RequestEmployeeContractDTO contractDTO);

    void addForEmployee(UserDetailContractDTO userDetailContractDTO);

    List<ResponseTotalDTO> getStatistical();

}

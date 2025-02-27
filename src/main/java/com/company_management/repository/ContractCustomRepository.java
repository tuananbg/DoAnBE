package com.company_management.repository;

import com.company_management.dto.ContractDTO;
import com.company_management.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ContractCustomRepository {
    PageResponse<ContractDTO> getAllContract(ContractDTO contractDTO, Pageable pageable);
}

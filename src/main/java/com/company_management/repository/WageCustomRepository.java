package com.company_management.repository;

import com.company_management.dto.WageDTO;
import com.company_management.dto.common.PageResponse;
import org.springframework.data.domain.Pageable;

public interface WageCustomRepository {
    PageResponse<WageDTO> getAllWage(WageDTO wageDTO, Pageable pageable);
}

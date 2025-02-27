package com.company_management.repository;

import com.company_management.dto.WageDTO;
import com.company_management.dto.response.DataPage;
import org.springframework.data.domain.Pageable;

public interface WageRepositoryCustom {
    DataPage<WageDTO> search(WageDTO wageDTO, Pageable pageable);
}

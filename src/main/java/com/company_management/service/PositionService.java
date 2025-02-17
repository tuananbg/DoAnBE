package com.company_management.service;

import com.company_management.dto.PositionDTO;
import com.company_management.dto.request.SearchPositionRequest;
import com.company_management.dto.response.DataPage;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;

public interface PositionService {

    DataPage<PositionDTO> search(SearchPositionRequest searchPositionRequest, Pageable pageable);

    PositionDTO detailPosition(Long id);

    void createOrUpdate(PositionDTO positionDTO);

    void deletePosition(Long id);

    ByteArrayInputStream exportExcel(SearchPositionRequest searchPositionRequest, Pageable pageable);

}

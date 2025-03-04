package com.company_management.service;

import com.company_management.dto.PositionDTO;
import com.company_management.dto.request.RequestPositionDTO;
import com.company_management.dto.request.SearchPositionRequest;
import com.company_management.dto.response.DataPage;
import com.company_management.dto.response.ResponsePositionDTO;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface PositionService {

    List<ResponsePositionDTO> getAllPositionSelection();

    ResponsePositionDTO detailPosition(Long id);

    void create(RequestPositionDTO positionDTO);

    void deletePosition(Long id);

//    ByteArrayInputStream exportExcel(SearchPositionRequest searchPositionRequest, Pageable pageable);

}

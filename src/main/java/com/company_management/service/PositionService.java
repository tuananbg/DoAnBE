package com.company_management.service;

import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.pa.RequestPositionDTO;
import com.company_management.dto.response.pa.ResponsePositionDTO;

import java.util.List;

public interface PositionService {

    List<ResponsePositionDTO> getAllPositionSelection();

    ResponsePositionDTO detailPosition(Long id);

    void create(RequestPositionDTO positionDTO);

    void deletePosition(Long id);

    ResponsePage<ResponsePositionDTO> getListByStatus(ObjectStatus status,String keyword, RequestPage page);

//    ByteArrayInputStream exportExcel(SearchPositionRequest searchPositionRequest, Pageable pageable);

}

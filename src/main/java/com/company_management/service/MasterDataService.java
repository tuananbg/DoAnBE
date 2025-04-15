package com.company_management.service;

import com.company_management.dto.request.RequestMasterDataDTO;
import com.company_management.dto.response.ResponseMasterDataDTO;

import java.util.List;

public interface MasterDataService {
    void createJobGroup(RequestMasterDataDTO request);

    List<ResponseMasterDataDTO> getListJobGroup();

    void createPositionCategory(RequestMasterDataDTO request);

    List<ResponseMasterDataDTO> getListPositionCategory();
}

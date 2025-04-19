package com.company_management.controller.pa;


import com.company_management.common.AppConstants;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.request.RequestMasterDataDTO;
import com.company_management.dto.response.ResponseMasterDataDTO;
import com.company_management.service.MasterDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/mdm")
@Slf4j
@RequiredArgsConstructor
public class MasterDataController {
    final MasterDataService masterDataService;
    @PostMapping(value = "/create/jobGroup")
    public BaseResponse<Object> createJobGroup(@RequestBody @Valid RequestMasterDataDTO request) {
        masterDataService.createJobGroup(request);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201, AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @GetMapping(value = "/list/jobGroup")
    public BaseResponse<List<ResponseMasterDataDTO>> getListJobGroup() {
        return BaseResponse.ok(masterDataService.getListJobGroup());
    }

    @PostMapping(value = "/create/positionCategory")
    public BaseResponse<Object> createPositionCategory(@RequestBody @Valid RequestMasterDataDTO request) {
        masterDataService.createPositionCategory(request);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201, AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @GetMapping(value = "/list/positionCategory")
    public BaseResponse<List<ResponseMasterDataDTO>> getListPositionCategory() {
        return BaseResponse.ok(masterDataService.getListPositionCategory());
    }
}

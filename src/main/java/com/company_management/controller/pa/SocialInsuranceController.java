package com.company_management.controller.pa;

import com.company_management.common.AppConstants;
import com.company_management.common.ResultResp;
import com.company_management.dto.SocialInsuranceDTO;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.request.pa.RequestSocialInsuranceDTO;
import com.company_management.service.SocialInsuranceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/social-insurance")
@Slf4j
@RequiredArgsConstructor
public class SocialInsuranceController {

    final SocialInsuranceService socialInsuranceService;

    @PostMapping(value = "/create")
    public BaseResponse<Object> create(@RequestBody @Valid RequestSocialInsuranceDTO request) {
        socialInsuranceService.create(request);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201,AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @GetMapping(value = "/list/employee-detail/{employeeCode}")
    public ResultResp<Object> getListEmployee(@PathVariable("employeeCode") String employeeCode, RequestPage pageable) {
        return ResultResp.success(socialInsuranceService.getListEmployee(employeeCode, pageable));
    }

//    @GetMapping(value = "/detail/{id}")
//    public ResultResp<Object> detail(@PathVariable Long id) {
//        return ResultResp.success(socialInsuranceService.detail(id));
//    }

    @PutMapping("/update")
    public BaseResponse<Object> update(@RequestBody @Valid RequestSocialInsuranceDTO request) {
        socialInsuranceService.update(request);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202,AppConstants.UPDATE_SUCCESS_MESS_202);
    }

    @DeleteMapping("/{id}")
    public ResultResp<Object> delete(@PathVariable Long id) {
        socialInsuranceService.deleteByIds(id);
        return ResultResp.success(null);
    }


}

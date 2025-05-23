package com.company_management.controller.pa;

import com.company_management.common.AppConstants;
import com.company_management.common.ErrorCode;
import com.company_management.common.ResultResp;
import com.company_management.dto.QualificationDTO;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.pa.RequestQualificationDTO;
import com.company_management.dto.response.pa.ResponseQualificationDTO;
import com.company_management.dto.response.pa.ResponseQualificationEmployeeDetailDTO;
import com.company_management.service.QualificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/qualification")
@Slf4j
@RequiredArgsConstructor
public class QualificationController {

    final QualificationService qualificationService;

    @PostMapping(value = "/create")
    public ResultResp<Object> create(@RequestBody @Valid RequestQualificationDTO request) {
        qualificationService.create(request);
        return ResultResp.success(ErrorCode.CREATED_OK);
    }

//    @PostMapping(value = "/search/{userDetailId}")
//    public ResultResp<Object> search(@PathVariable("userDetailId") Long userDetailId,Pageable pageable) {
//        return ResultResp.success(qualificationService.search(userDetailId, pageable));
//    }

    @GetMapping(value = "/{id}")
    public BaseResponse<ResponseQualificationDTO> detail(@PathVariable Long id) {
        return BaseResponse.ok(qualificationService.detail(id));
    }

    @PutMapping
    public ResultResp<Object> update(@RequestBody @Valid QualificationDTO qualificationDTO) {
        qualificationService.update(qualificationDTO);
        return ResultResp.success(null);
    }

//    @DeleteMapping("/{id}")
//    public ResultResp<Object> delete(@PathVariable Long id) {
//        qualificationService.deleteByIds(id);
//        return ResultResp.success(null);
//    }

    @GetMapping(value = "/employee-detail/{employeeCode}")
    public BaseResponse<ResponsePage<ResponseQualificationEmployeeDetailDTO>> getDetail(@PathVariable("employeeCode") String employeeCode, RequestPage page) {

        return BaseResponse.ok(AppConstants.GET_CODE_200, AppConstants.GET_MESSAGE_200,qualificationService.getDetailEmployees(employeeCode,page));
    }


}

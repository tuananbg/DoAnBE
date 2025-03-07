package com.company_management.controller.HRM;

import com.company_management.common.AppConstants;
import com.company_management.common.ErrorCode;
import com.company_management.common.ResultResp;
import com.company_management.dto.QualificationDTO;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.response.ResponseQualificationEmployeeDetailDTO;
import com.company_management.service.QualificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/qualification")
@Slf4j
@RequiredArgsConstructor
public class QualificationController {

    final QualificationService qualificationService;

        @PostMapping(value = "/create")
    public ResultResp<Object> create(@RequestBody @Valid QualificationDTO qualificationDTO) {
        qualificationService.addQualification(qualificationDTO);
        return ResultResp.success(ErrorCode.CREATED_OK);
    }

//    @PostMapping(value = "/search/{userDetailId}")
//    public ResultResp<Object> search(@PathVariable("userDetailId") Long userDetailId,Pageable pageable) {
//        return ResultResp.success(qualificationService.search(userDetailId, pageable));
//    }

    @GetMapping(value = "/detail/{id}")
    public ResultResp<Object> detail(@PathVariable Long id) {
        return ResultResp.success(qualificationService.detail(id));
    }

    @PutMapping
    public ResultResp<Object> update(@RequestBody @Valid QualificationDTO qualificationDTO) {
        qualificationService.update(qualificationDTO);
        return ResultResp.success(null);
    }

    @DeleteMapping("/{id}")
    public ResultResp<Object> delete(@PathVariable Long id) {
        qualificationService.deleteByIds(id);
        return ResultResp.success(null);
    }

    @GetMapping(value = "/employee-detail/{userDetailId}")
    public BaseResponse<List<ResponseQualificationEmployeeDetailDTO>> getDetail(@PathVariable("userDetailId") Long userDetailId) {

        return BaseResponse.ok(AppConstants.STATUS_200, AppConstants.MESSAGE_200,qualificationService.getDetailEmployees(userDetailId));
    }


}

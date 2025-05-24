package com.company_management.controller.pa;

import com.company_management.common.AppConstants;
import com.company_management.common.ErrorCode;
import com.company_management.common.ResultResp;
import com.company_management.common.enums.ContractStatusEnum;
import com.company_management.common.enums.ObjectStatus;
import com.company_management.common.enums.ReportType;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.pa.RequestPositionDTO;
import com.company_management.dto.response.pa.ResponsePositionDTO;
import com.company_management.service.PositionService;
import com.company_management.service.common.JasperReportService;
import com.company_management.utils.CommonUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/position")
public class PositionController {

    private final PositionService positionService;
    private final JasperReportService jasperReportService;

    @GetMapping("/list/selection")
    public BaseResponse<List<ResponsePositionDTO>> getAllPosition() {
        return BaseResponse.ok(AppConstants.GET_CODE_200, AppConstants.GET_MESSAGE_200, positionService.getAllPositionSelection());
    }

    @PostMapping("/create")
    public BaseResponse<Object> createPosition(@Valid @RequestBody RequestPositionDTO positionDTO) {
        positionService.create(positionDTO);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201, AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @GetMapping("/detail/{id}")
    public ResultResp<Object> getByIdPosition(@PathVariable("id") Long id) {
        return ResultResp.success(ErrorCode.CREATED_OK, positionService.detailPosition(id));
    }

    @GetMapping("/list/{status}")
    public BaseResponse<ResponsePage<ResponsePositionDTO>> getAllPositions(@RequestParam(name = "keyword", required = false) String keyword,
                                                                           @PathVariable("status") ObjectStatus status, RequestPage page) {
        return BaseResponse.ok(positionService.getListByStatus(status,keyword,page));
    }

    @PutMapping("/update")
    public BaseResponse<Object> updatePosition(@Valid @RequestBody RequestPositionDTO request) {
        positionService.update(request);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202, AppConstants.UPDATE_SUCCESS_MESS_202);
    }

    @PutMapping("/lock/{positionCode}")
    public BaseResponse<Object> disable(@PathVariable("positionCode") String positionCode) {
        positionService.disable(positionCode);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202, AppConstants.UPDATE_SUCCESS_MESS_202);
    }

    @PutMapping("/unlock/{positionCode}")
    public BaseResponse<Object> unlock(@PathVariable("positionCode") String positionCode) {
        positionService.unlock(positionCode);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202, AppConstants.UPDATE_SUCCESS_MESS_202);
    }

    @GetMapping(value = "/download-xlsx/{status}")
    public ResponseEntity<Resource> download(@PathVariable("status") ObjectStatus status) {
        byte[] bytes = jasperReportService.positionStatus(status);
        String fileName = "DTDI_HRM_Danh sach chuc vu_" + CommonUtils.getCurrentDate("ddMMyyyy") + "." + ReportType.XLSX.getCode();
        return jasperReportService.baseDownload(bytes, fileName);
    }

}

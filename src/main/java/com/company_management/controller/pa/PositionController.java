package com.company_management.controller.pa;

import com.company_management.common.AppConstants;
import com.company_management.common.ErrorCode;
import com.company_management.common.ResultResp;
import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.pa.RequestPositionDTO;
import com.company_management.dto.response.pa.ResponsePositionDTO;
import com.company_management.service.PositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/position")
public class PositionController {

    private final PositionService positionService;

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

//    @PutMapping
//    public ResultResp<Object> updatePosition(@Valid @RequestBody PositionDTO positionDTO) {
//        positionService.createOrUpdate(positionDTO);
//        return ResultResp.success(ErrorCode.UPDATED_OK, null);
//    }

    @DeleteMapping("/delete/{id}")
    public ResultResp<Object> deletePosition(@PathVariable("id") Long id) {
        positionService.deletePosition(id);
        return ResultResp.success(ErrorCode.DELETED_OK, null);
    }
//
//    @PostMapping(value = "/export")
//    public ResponseEntity<Object> exportExcel(@RequestBody SearchPositionRequest searchPositionRequest, Pageable pageable) {
//        ByteArrayInputStream result = positionService.exportExcel(searchPositionRequest, pageable);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        String fileName = CommonUtils.getFileNameReportUpdate("EXPORT_POSITION");
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
//        return new ResponseEntity<>(new InputStreamResource(result), headers, HttpStatus.OK);
//    }

}

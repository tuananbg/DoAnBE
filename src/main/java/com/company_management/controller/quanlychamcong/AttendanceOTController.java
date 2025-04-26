package com.company_management.controller.quanlychamcong;

import com.company_management.common.AppConstants;
import com.company_management.common.ErrorCode;
import com.company_management.common.ResultResp;
import com.company_management.common.enums.ContractStatusEnum;
import com.company_management.common.enums.TableTabType;
import com.company_management.dto.AttendanceOTDTO;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.attendace.RequestAttendanceOTDTO;
import com.company_management.dto.request.attendace.RequestUpdateAttendanceOTDTO;
import com.company_management.dto.request.pa.SearchAttendanceOTRequest;
import com.company_management.dto.response.attendance.ResponseAttendanceOTDTO;
import com.company_management.service.AttendanceOTService;
import com.company_management.utils.CommonUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/attendance-ot")
public class AttendanceOTController {

    private final AttendanceOTService attendanceOTService;

    @GetMapping("/list/{status}")
    public BaseResponse<ResponsePage<ResponseAttendanceOTDTO>> getList(@RequestParam(name = "keyword", required = false) String keyword,
                                                                       @PathVariable("status") TableTabType status,
                                                                       RequestPage page) {
        return BaseResponse.ok(attendanceOTService.getList(status,keyword, page));
    }

    @PostMapping("/create")
    public BaseResponse<Object> createOT(@Valid @RequestBody RequestAttendanceOTDTO request) {
        attendanceOTService.create(request);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201,AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @GetMapping("/detail/{id}")
    public ResultResp<Object> getByIdOT(@PathVariable("id") Long id) {
        return ResultResp.success(ErrorCode.CREATED_OK, attendanceOTService.detailOT(id));
    }

    @PutMapping("/update")
    public ResultResp<Object> updateOT(@Valid @RequestBody RequestUpdateAttendanceOTDTO request) {
        attendanceOTService.update(request);
        return ResultResp.success(ErrorCode.UPDATED_OK, null);
    }
    @PutMapping("/complete")
    public ResultResp<Object> complete(@Valid @RequestBody RequestUpdateAttendanceOTDTO request) {
        attendanceOTService.complete(request);
        return ResultResp.success(ErrorCode.UPDATED_OK, null);
    }


    @PostMapping(value = "/download")
    public ResponseEntity<Object> exportExcel(@RequestBody SearchAttendanceOTRequest searchAttendanceOTRequest, Pageable pageable) {
        ByteArrayInputStream result = attendanceOTService.exportExcel(searchAttendanceOTRequest, pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String fileName = CommonUtils.getFileNameReportUpdate("EXPORT_OT");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return new ResponseEntity<>(new InputStreamResource(result), headers, HttpStatus.OK);
    }

//    @DeleteMapping("/delete/{id}")
//    public ResultResp<Object> deleteOT(@PathVariable("id") Long id) {
//        attendanceOTService.deleteOT(id);
//        return ResultResp.success(ErrorCode.DELETED_OK, null);
//    }



}

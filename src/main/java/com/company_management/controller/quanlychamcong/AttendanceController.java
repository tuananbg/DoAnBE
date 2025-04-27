package com.company_management.controller.quanlychamcong;

import com.company_management.common.AppConstants;
import com.company_management.common.ErrorCode;
import com.company_management.common.ResultResp;
import com.company_management.dto.AttendanceDTO;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.request.attendace.RequestAttendanceDTO;
import com.company_management.dto.request.pa.SearchAttendanceRequest;
import com.company_management.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("${apiPrefix}/attendance")
@Slf4j
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    @PostMapping("/search")
    public ResultResp<Object> searchAttendance(@RequestBody SearchAttendanceRequest searchAttendanceRequest,
                                             Pageable pageable) {
        return ResultResp.success(attendanceService.search(searchAttendanceRequest, pageable));
    }

    @PostMapping("/create")
    public BaseResponse<Object> createAttendance(@RequestBody RequestAttendanceDTO request) {
        attendanceService.create(request);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201,AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @PostMapping("/update")
    public ResultResp<Object> updateAttendance(@RequestBody  AttendanceDTO attendanceDTO) {
        attendanceService.createOrUpdate(attendanceDTO);
        return ResultResp.success(ErrorCode.UPDATED_OK, null);
    }

    @GetMapping("/getId/{employeeCode}")
    public BaseResponse<Object> getDetailAttendanceId(@PathVariable("employeeCode") String employeeCode) {
        return BaseResponse.ok(attendanceService.detailAttendanceId(employeeCode));
    }

}

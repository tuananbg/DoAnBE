package com.company_management.controller.quanlychamcong;

import com.company_management.common.AppConstants;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.attendace.RequestAttendanceDTO;
import com.company_management.dto.request.pa.SearchAttendanceRequest;
import com.company_management.dto.response.attendance.ResponseAttendanceDTO;
import com.company_management.dto.response.attendance.ResponseAttendanceStatusDTO;
import com.company_management.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/attendance")
@Slf4j
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    @PostMapping("/list")
    public BaseResponse<ResponsePage<ResponseAttendanceDTO>> getList(RequestPage page, @RequestBody SearchAttendanceRequest search) {
        return BaseResponse.ok(attendanceService.getList(page,search));
    }

    @PostMapping("/create")
    public BaseResponse<Object> createAttendance(@RequestBody RequestAttendanceDTO request) {
        attendanceService.create(request);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201,AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @PostMapping("/update")
    public BaseResponse<Object> updateAttendance(@RequestBody RequestAttendanceDTO request) {
        attendanceService.update(request);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202,AppConstants.UPDATE_SUCCESS_MESS_202);
    }

    @GetMapping("/getId/{employeeCode}")
    public BaseResponse<ResponseAttendanceStatusDTO> getDetailAttendanceId(@PathVariable("employeeCode") String employeeCode) {
        return BaseResponse.ok(attendanceService.getAttendanceId(employeeCode));
    }

}

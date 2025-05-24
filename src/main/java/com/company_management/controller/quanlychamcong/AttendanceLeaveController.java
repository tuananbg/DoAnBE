package com.company_management.controller.quanlychamcong;

import com.company_management.common.AppConstants;
import com.company_management.common.ErrorCode;
import com.company_management.common.ResultResp;
import com.company_management.common.enums.AttendanceLeaveStatus;
import com.company_management.common.enums.ReportType;
import com.company_management.common.enums.TableTabType;
import com.company_management.common.enums.TaskStatusEnum;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.attendace.RequestAttendanceLeaveDTO;
import com.company_management.dto.request.attendace.RequestUpdateAttendanceLeaveDTO;
import com.company_management.dto.request.attendace.SearchLeaveRequest;
import com.company_management.dto.response.attendance.ResponseAttendanceLeaveDTO;
import com.company_management.entity.Attendance;
import com.company_management.service.AttendanceLeaveService;
import com.company_management.service.common.JasperReportService;
import com.company_management.utils.CommonUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/attendance-leave")
public class AttendanceLeaveController {

    private final AttendanceLeaveService attendanceLeaveService;
    private final JasperReportService jasperReportService;

    @GetMapping("/list/{status}")
    public BaseResponse<ResponsePage<ResponseAttendanceLeaveDTO>> searchLeave(@RequestParam(name = "keyword", required = false) String keyword,
                                                                              @PathVariable("status") TableTabType status,
                                                                              @ModelAttribute @Valid RequestPage page) {
        return BaseResponse.ok(attendanceLeaveService.search(status, keyword, page));
    }

    @PostMapping("/create")
    public BaseResponse<Object> createLeave(@Valid @RequestBody RequestAttendanceLeaveDTO leaveDTO) {
        attendanceLeaveService.create(leaveDTO);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201, AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @PutMapping("/update")
    public BaseResponse<Object> updateLeave(@Valid @RequestBody RequestUpdateAttendanceLeaveDTO request) {
        attendanceLeaveService.update(request);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202, AppConstants.UPDATE_SUCCESS_MESS_202);
    }

    @PutMapping("/complete")
    public BaseResponse<Object> complete(@Valid @RequestBody RequestUpdateAttendanceLeaveDTO request) {
        attendanceLeaveService.complete(request);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202, AppConstants.UPDATE_SUCCESS_MESS_202);
    }

    @GetMapping(value = "/download-xlsx/{status}")
    public ResponseEntity<Resource> download(@PathVariable("status") AttendanceLeaveStatus status) {
        byte[] bytes = jasperReportService.attendanceLeave(status);
        String fileName = "DTDI_HRM_Danh sach don nghi phep_" + CommonUtils.getCurrentDate("ddMMyyyy") + "." + ReportType.XLSX.getCode();
        return jasperReportService.baseDownload(bytes, fileName);
    }

}

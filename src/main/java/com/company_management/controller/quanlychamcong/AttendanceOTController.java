package com.company_management.controller.quanlychamcong;

import com.company_management.common.AppConstants;
import com.company_management.common.enums.AttendanceLeaveStatus;
import com.company_management.common.enums.ReportType;
import com.company_management.common.enums.TableTabType;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.attendace.RequestAttendanceOTDTO;
import com.company_management.dto.request.attendace.RequestUpdateAttendanceOTDTO;
import com.company_management.dto.response.attendance.ResponseAttendanceOTDTO;
import com.company_management.service.AttendanceOTService;
import com.company_management.service.common.JasperReportService;
import com.company_management.utils.CommonUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/attendance-ot")
public class AttendanceOTController {

    private final AttendanceOTService attendanceOTService;

    private final JasperReportService jasperReportService;

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

//    @GetMapping("/detail/{id}")
//    public ResultResp<Object> getByIdOT(@PathVariable("id") Long id) {
//        return ResultResp.success(ErrorCode.CREATED_OK, attendanceOTService.detailOT(id));
//    }

    @PutMapping("/update")
    public BaseResponse<Object> updateOT(@Valid @RequestBody RequestUpdateAttendanceOTDTO request) {
        attendanceOTService.update(request);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202, AppConstants.UPDATE_SUCCESS_MESS_202);
    }
    @PutMapping("/complete")
    public BaseResponse<Object> complete(@Valid @RequestBody RequestUpdateAttendanceOTDTO request) {
        attendanceOTService.complete(request);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202, AppConstants.UPDATE_SUCCESS_MESS_202);
    }


    @GetMapping(value = "/download-xlsx/{status}")
    public ResponseEntity<Resource> download(@PathVariable("status") AttendanceLeaveStatus status) {
        byte[] bytes = jasperReportService.attendanceOt(status);
        String fileName = "DTDI_HRM_Danh sach don tang ca_" + CommonUtils.getCurrentDate("ddMMyyyy") + "." + ReportType.XLSX.getCode();
        return jasperReportService.baseDownload(bytes, fileName);
    }



}

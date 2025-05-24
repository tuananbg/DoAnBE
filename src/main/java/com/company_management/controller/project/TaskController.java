package com.company_management.controller.project;

import com.company_management.common.AppConstants;
import com.company_management.common.enums.ContractStatusEnum;
import com.company_management.common.enums.ReportType;
import com.company_management.common.enums.TaskStatusEnum;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.projcet.RequestCreateTaskDTO;
import com.company_management.dto.request.projcet.RequestUpdateTaskDTO;
import com.company_management.dto.response.project.ResponseDetailTaskDTO;
import com.company_management.dto.response.project.ResponseListTaskDTO;
import com.company_management.dto.response.project.ResponseProjectDashboardTO;
import com.company_management.exception.AppException;
import com.company_management.service.TaskService;
import com.company_management.service.common.JasperReportService;
import com.company_management.utils.CommonUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/task")
@Slf4j
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final JasperReportService jasperReportService;

    @PostMapping(value = "/create")
    public BaseResponse<Object> create(@RequestBody  @Valid RequestCreateTaskDTO request) {
        taskService.createTask(request);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201, AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @GetMapping(value = "/list/{status}")
    public BaseResponse<ResponsePage<ResponseListTaskDTO>> getList(@RequestParam(name = "keyword", required = false) String keyword,
                                                                   @PathVariable("status") TaskStatusEnum status, RequestPage page) {
        return BaseResponse.ok(taskService.getTasks(status, keyword, page));
    }

    @GetMapping(value = "/list/person/{status}")
    public BaseResponse<ResponsePage<ResponseListTaskDTO>> getListPerson(@RequestParam(name = "keyword", required = false) String keyword,
                                                                   @PathVariable("status") TaskStatusEnum status, RequestPage page) {
        return BaseResponse.ok(taskService.getListPerson(status, keyword, page));
    }

    @GetMapping(value = "/dashboard")
    public BaseResponse<List<ResponseProjectDashboardTO>> getListDashboard() {
        return BaseResponse.ok(taskService.getListDashboard());
    }

    @GetMapping(value = "/detail/{code}")
    public BaseResponse<ResponseDetailTaskDTO> getListDashboard(@PathVariable("code") String code) {
        return BaseResponse.ok(taskService.getDetailTask(code));
    }

    @PutMapping(value = "/update")
    public BaseResponse<Object> update(@RequestBody  @Valid RequestUpdateTaskDTO request) {
        try {
            taskService.updateTask(request);
            return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202, AppConstants.UPDATE_SUCCESS_CODE_202);
        } catch (AppException ex) {
            return BaseResponse.error(AppConstants.CODE_400, ex.getMessage());
        }
    }

    @GetMapping(value = "/download-xlsx/{status}")
    public ResponseEntity<Resource> download(@PathVariable("status") TaskStatusEnum status) {
        byte[] bytes = jasperReportService.taskStatus(status);
        String fileName = "DTDI_HRM_Danh sach nhiem vu_" + CommonUtils.getCurrentDate("ddMMyyyy") + "." + ReportType.XLSX.getCode();
        return jasperReportService.baseDownload(bytes, fileName);
    }
}

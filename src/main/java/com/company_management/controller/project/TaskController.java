package com.company_management.controller.project;

import com.company_management.common.AppConstants;
import com.company_management.common.enums.TaskStatusEnum;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.projcet.RequestCreateTaskDTO;
import com.company_management.dto.response.project.ResponseListTaskDTO;
import com.company_management.dto.response.project.ResponseProjectDashboardTO;
import com.company_management.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/task")
@Slf4j
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
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

    @GetMapping(value = "/dashboard")
    public BaseResponse<List<ResponseProjectDashboardTO>> getListDashboard() {
        return BaseResponse.ok(taskService.getListDashboard());
    }
}

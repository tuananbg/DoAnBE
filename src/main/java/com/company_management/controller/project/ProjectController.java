package com.company_management.controller.project;

import com.company_management.common.AppConstants;
import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.projcet.RequestProjectDTO;
import com.company_management.dto.response.project.ResponseDetailTaskDTO;
import com.company_management.dto.response.project.ResponseListProjectDTO;
import com.company_management.dto.response.project.ResponseListTaskOfProjectDTO;
import com.company_management.dto.response.project.ResponseSelectProjectDTO;
import com.company_management.service.ProjectService;
import com.company_management.utils.annotation.PasswordMatching;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/project")
@Slf4j
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping(value = "/create")
    public BaseResponse<Object> create(@ModelAttribute @Valid RequestProjectDTO request) {
        projectService.create(request);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201, AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @GetMapping(value = "/list/{status}")
    public BaseResponse<ResponsePage<ResponseListProjectDTO>> getList(@RequestParam(name = "keyword", required = false) String keyword,
                                                                      @PathVariable("status") ObjectStatus status, RequestPage page) {
        return BaseResponse.ok(projectService.getList(status, keyword, page));
    }

    @GetMapping(value = "/select")
    public BaseResponse<List<ResponseSelectProjectDTO>> select() {
        return BaseResponse.ok(projectService.getListSelect());
    }

    @GetMapping(value = "/detail/task/{id}")
    public BaseResponse<List<ResponseListTaskOfProjectDTO>> getDetailTask(@PathVariable("id") long id) {
        return BaseResponse.ok(projectService.getListTask(id));
    }

}

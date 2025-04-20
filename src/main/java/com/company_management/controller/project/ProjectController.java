package com.company_management.controller.project;

import com.company_management.common.AppConstants;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.request.projcet.RequestProjectDTO;
import com.company_management.dto.request.projcet.RequestUpdateTaskDTO;
import com.company_management.dto.response.project.*;
import com.company_management.exception.AppException;
import com.company_management.service.ProjectService;
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

    @GetMapping(value = "/list")
    public BaseResponse<List<ResponseListProjectDTO>> getList() {
        return BaseResponse.ok(projectService.getList());
    }

    @GetMapping(value = "/select")
    public BaseResponse<List<ResponseSelectProjectDTO>> select() {
        return BaseResponse.ok(projectService.getListSelect());
    }

    @GetMapping(value = "/detail/task/{id}")
    public BaseResponse<List<ResponseListTaskOfProjectDTO>> getDetailTask(@PathVariable("id") long id) {
        return BaseResponse.ok(projectService.getListTask(id));
    }

    @GetMapping(value = "/detail/{id}")
    public BaseResponse<ResponseDetailProjectDTO> getDetail(@PathVariable("id") long id) {
        return BaseResponse.ok(projectService.getDetail(id));
    }
    @PutMapping(value = "/update")
    public BaseResponse<Object> update(@RequestBody @Valid RequestProjectDTO request) {
        try {
            projectService.update(request);
            return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202, AppConstants.UPDATE_SUCCESS_CODE_202);
        } catch (AppException ex) {
            return BaseResponse.error(AppConstants.CODE_400, ex.getMessage());
        }
    }


}

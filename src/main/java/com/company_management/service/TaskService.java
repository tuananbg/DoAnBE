package com.company_management.service;

import com.company_management.common.enums.TaskStatusEnum;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.projcet.RequestCreateTaskDTO;
import com.company_management.dto.request.projcet.RequestUpdateTaskDTO;
import com.company_management.dto.response.project.ResponseDetailTaskDTO;
import com.company_management.dto.response.project.ResponseListTaskDTO;
import com.company_management.dto.response.project.ResponseProjectDashboardTO;

import java.util.List;

public interface TaskService {
    void createTask(RequestCreateTaskDTO request);

    ResponsePage<ResponseListTaskDTO> getTasks(TaskStatusEnum status, String keyword, RequestPage page);

    List<ResponseProjectDashboardTO> getListDashboard();

    ResponseDetailTaskDTO getDetailTask(String code);

    void updateTask(RequestUpdateTaskDTO request);
}

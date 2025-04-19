package com.company_management.service;

import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.projcet.RequestCreateTaskDTO;
import com.company_management.dto.response.project.ResponseListTaskDTO;

public interface TaskService {
    void createTask(RequestCreateTaskDTO request);

    ResponsePage<ResponseListTaskDTO> getTasks(Object status, String keyword, RequestPage page);
}

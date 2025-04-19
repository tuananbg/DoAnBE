package com.company_management.service.impl;

import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.projcet.RequestCreateTaskDTO;
import com.company_management.dto.response.project.ResponseListTaskDTO;
import com.company_management.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Override
    public void createTask(RequestCreateTaskDTO request) {
        
    }

    @Override
    public ResponsePage<ResponseListTaskDTO> getTasks(Object status, String keyword, RequestPage page) {
        return null;
    }


}

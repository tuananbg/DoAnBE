package com.company_management.service;

import com.company_management.dto.request.projcet.RequestProjectDTO;
import com.company_management.dto.response.project.ResponseListProjectDTO;
import com.company_management.dto.response.project.ResponseListTaskOfProjectDTO;
import com.company_management.dto.response.project.ResponseSelectProjectDTO;

import java.util.List;

public interface ProjectService {
    void create(RequestProjectDTO request);

    List<ResponseListProjectDTO> getList();

    List<ResponseSelectProjectDTO> getListSelect();

    List<ResponseListTaskOfProjectDTO> getListTask(long id);

}

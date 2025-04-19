package com.company_management.service.impl;

import com.company_management.common.AppConstants;
import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.projcet.RequestProjectDTO;
import com.company_management.dto.response.project.ResponseListProjectDTO;
import com.company_management.entity.Project;
import com.company_management.exception.AppException;
import com.company_management.repository.ProjectRepository;
import com.company_management.service.ProjectService;
import com.company_management.utils.mapper.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public void create(RequestProjectDTO request) {
        checkProjectCode(request.getProjectCode());
        Project project = new Project();
        MapperUtils.map(request, project);
        projectRepository.save(project);
        log.debug("Create project: {}", project.getProjectCode());
    }

    @Override
    public ResponsePage<ResponseListProjectDTO> getList(ObjectStatus status, String keyword, RequestPage page) {
        Page<Project> projectPage = projectRepository.findAllByIsActiveAndKeyword(status.getCode(),keyword,page.toPageable());
        List<ResponseListProjectDTO> data = projectPage.getContent().stream().map(item -> {
            ResponseListProjectDTO dto = new ResponseListProjectDTO();
            MapperUtils.map(item, dto);
            return dto;
        }).toList();
        return new ResponsePage<>(data,page,projectPage.getTotalElements());
    }

    private void checkProjectCode(String projectCode) {
        if (projectCode != null) {
            if (!projectRepository.existsByProjectCode(projectCode)){
                throw new AppException(AppConstants.PROJECT_CODE_EXIST_CODE_001,AppConstants.PROJECT_CODE_EXIST_MESS_001);
            }
        }
    }
}

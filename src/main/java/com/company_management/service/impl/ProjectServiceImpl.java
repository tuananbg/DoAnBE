package com.company_management.service.impl;

import com.company_management.common.AppConstants;
import com.company_management.common.enums.ObjectStatus;
import com.company_management.common.enums.TaskStatusEnum;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.projcet.RequestProjectDTO;
import com.company_management.dto.response.project.*;
import com.company_management.entity.Project;
import com.company_management.entity.Task;
import com.company_management.exception.AppException;
import com.company_management.repository.ProjectRepository;
import com.company_management.repository.TaskRepository;
import com.company_management.service.ProjectService;
import com.company_management.utils.mapper.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

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

    @Override
    public List<ResponseSelectProjectDTO> getListSelect() {
        List<Project> projects = projectRepository.findAll();
        List<ResponseSelectProjectDTO> data = new ArrayList<>();
        for (Project project : projects) {
            ResponseSelectProjectDTO dto = new ResponseSelectProjectDTO();
            MapperUtils.map(project, dto);
            data.add(dto);
        }
        return data;
    }

    @Override
    public List<ResponseListTaskOfProjectDTO> getListTask(long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new AppException("ERR01","Dự án không tồn tại!"));
        List<Task> tasks = taskRepository.findByProjectCode(project.getProjectCode());
        List<Integer> statusList = tasks.stream().map(Task::getStatus).toList();
        List<ResponseListTaskOfProjectDTO> data = new ArrayList<>();
        for (Integer status : statusList) {
            ResponseListTaskOfProjectDTO dto = new ResponseListTaskOfProjectDTO();
            dto.setName(TaskStatusEnum.findByCode(status).getName());
            List<ResponseDetailTaskDTO> taskDTOList = new ArrayList<>();
            for (Task task : tasks) {
                if (task.getStatus().equals(status)) {
                    ResponseDetailTaskDTO taskDTO = new ResponseDetailTaskDTO();
                    taskDTO.setTaskName(task.getTaskName());
                    taskDTO.setId(task.getId());
                    taskDTO.setTaskStatusName(TaskStatusEnum.findByCode(status).getName());
                    taskDTO.setDescription(task.getTaskDescription());
                    taskDTOList.add(taskDTO);
                }
            }
            dto.setTaskForm(taskDTOList);
            data.add(dto);
        }
        return data;
    }

    @Override
    public List<ResponseProjectDashboardTO> getListDashboard() {
        List<Project> projects = projectRepository.findAll();
        List<ResponseProjectDashboardTO> data = new ArrayList<>();
        long taskAll = taskRepository.countAllTasks();
        for (Project project : projects) {
            ResponseProjectDashboardTO dto = new ResponseProjectDashboardTO();
            dto.setProjectName(project.getProjectName());

            long tasksOfProject = taskRepository.countTaskByManagerCode(project.getProjectCode());
            dto.setNumberOfTasks((int) tasksOfProject);

            long percent = tasksOfProject / taskAll;

            dto.setPercentage(percent);
            data.add(dto);
        }
        return data;
    }

    private void checkProjectCode(String projectCode) {
        if (projectCode != null) {
            if (projectRepository.existsByProjectCode(projectCode)){
                throw new AppException(AppConstants.PROJECT_CODE_EXIST_CODE_001,AppConstants.PROJECT_CODE_EXIST_MESS_001);
            }
        }
    }
}

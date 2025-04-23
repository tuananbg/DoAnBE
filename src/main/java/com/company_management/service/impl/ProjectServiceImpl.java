package com.company_management.service.impl;

import com.company_management.common.AppConstants;
import com.company_management.common.enums.ProjectStatus;
import com.company_management.common.enums.TaskStatusEnum;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(RequestProjectDTO request) {
        checkProjectCode(request.getProjectCode());
        Project project = new Project();
        MapperUtils.map(request, project);
        projectRepository.save(project);
        log.debug("Create project: {}", project.getProjectCode());
    }

    @Override
    @Transactional
    public List<ResponseListProjectDTO> getList() {
        List<Project> projectPage = projectRepository.findAll();
        List<ResponseListProjectDTO> data = new ArrayList<>();
        for (Project project : projectPage) {
            ResponseListProjectDTO dto = new ResponseListProjectDTO();
            Object[] result =taskRepository.countTaskByProjectCode(project.getProjectCode());
            Object[] row = (Object[]) result[0];
            String todo = row[0] != null ? row[0].toString() : "0";
            String process = row[1] != null ? row[1].toString() : "0";
            String done = row[2] != null ? row[2].toString() : "0";

            dto.setTaskTodo(todo);
            dto.setTaskProcess(process);
            dto.setTaskDone(done);

            ProjectStatus status = ProjectStatus.findByCode(project.getStatus());
            if (status != null) {
                dto.setProjectStatus(status.getDescription());
            }

            MapperUtils.map(project, dto);
            data.add(dto);
        }
        return data;
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
            List<ResponseDetailListTaskDTO> taskDTOList = new ArrayList<>();
            for (Task task : tasks) {
                if (task.getStatus().equals(status)) {
                    ResponseDetailListTaskDTO taskDTO = new ResponseDetailListTaskDTO();
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
    public ResponseDetailProjectDTO getDetail(long id) {
        Project project = projectRepository.findById(id).orElseThrow(()->new AppException("ERR1","Dự án không tồn tại trong hệ thống!"));
        ResponseDetailProjectDTO dto = new ResponseDetailProjectDTO();
        MapperUtils.map(project, dto);
        return dto;

    }

    @Override
    public void update(RequestProjectDTO request) {
        Project project = projectRepository.findByProjectCode(request.getProjectCode()).orElseThrow(()->new AppException("ERR01","Dự án không tồn tại trong hệ thống !"));
        MapperUtils.mapOnlyNotNullProperty(request, project);
        projectRepository.save(project);
    }

    private void checkProjectCode(String projectCode) {
        if (projectCode != null) {
            if (projectRepository.existsByProjectCode(projectCode)){
                throw new AppException(AppConstants.PROJECT_CODE_EXIST_CODE_001,AppConstants.PROJECT_CODE_EXIST_MESS_001);
            }
        }
    }
}

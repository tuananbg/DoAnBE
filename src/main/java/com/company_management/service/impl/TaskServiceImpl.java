package com.company_management.service.impl;

import com.company_management.common.enums.TaskStatusEnum;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.projcet.RequestCreateTaskDTO;
import com.company_management.dto.response.project.ResponseListTaskDTO;
import com.company_management.dto.response.project.ResponseProjectDashboardTO;
import com.company_management.entity.Employee;
import com.company_management.entity.Project;
import com.company_management.entity.Task;
import com.company_management.exception.AppException;
import com.company_management.repository.EmployeeRepository;
import com.company_management.repository.ProjectRepository;
import com.company_management.repository.TaskRepository;
import com.company_management.service.TaskService;
import com.company_management.utils.mapper.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    @Override
    public void createTask(RequestCreateTaskDTO request) {
        checkTaskCode(request.getTaskCode());
        Task task = new Task();
        MapperUtils.map(request,task);
        Employee employee = employeeRepository.findByCode(request.getEmployeeCode())
                .orElseThrow(()-> new AppException("ER001","Cán bộ nhân viên không tồn tại trong hệ thống!"));
        task.setEmployee(employee);

        Project project = projectRepository.findByProjectCode(request.getProjectCode())
                .orElseThrow(()-> new AppException("ER002","Dự án không tồn tại trong hệ thống!"));
        task.setProject(project);
        taskRepository.save(task);
    }

    @Override
    public ResponsePage<ResponseListTaskDTO> getTasks(TaskStatusEnum status, String keyword, RequestPage page) {
        Page<Task> taskPage = taskRepository.findByStatus(status.getCode(),keyword,page.toPageable());
        List<ResponseListTaskDTO> data = taskPage.getContent()
                .stream()
                .map(item ->{
                    ResponseListTaskDTO dto = new ResponseListTaskDTO();
                    Project project = item.getProject();
                    if(project != null) {
                        dto.setProjectName(project.getProjectName());
                    }
                    Employee employee = item.getEmployee();
                    if(employee != null) {
                        dto.setEmployeeName(employee.getFullName());
                    }
                    employeeRepository.findByCode(item.getManagerCode())
                            .ifPresent(empManager -> dto.setManagerName(empManager.getFullName()));
                    MapperUtils.map(item,dto);
                    return dto;
                }).toList();
        return new ResponsePage<>(data,page,taskPage.getTotalElements());
    }

    private void checkTaskCode(String taskCode) {
        if (taskRepository.existsByTaskCode(taskCode)) {
            throw new AppException("ERR","Mã nhiệm vụ đã tồn tại");
        }
    }
    @Override
    public List<ResponseProjectDashboardTO> getListDashboard() {
        List<Project> projects = projectRepository.findAll();
        List<ResponseProjectDashboardTO> data = new ArrayList<>();
        for (Project project : projects) {
            ResponseProjectDashboardTO dto = new ResponseProjectDashboardTO();
            dto.setProjectName(project.getProjectName());
            Object[] result = taskRepository.countTaskAndDoneByProjectCode(project.getProjectCode(),TaskStatusEnum.DONE.getCode());
            long tasksOfProject = Long.parseLong(result[0].toString());
            long taskDoneOfProject = Long.parseLong(result[1].toString());
            dto.setNumberOfTasks((int) tasksOfProject);
            dto.setNumberOfTasksDone((int) taskDoneOfProject);
            long percent = taskDoneOfProject / tasksOfProject;
            dto.setPercentage(percent);
            data.add(dto);
        }
        return data;
    }


}

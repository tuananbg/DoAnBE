package com.company_management.service.impl;

import com.company_management.common.Constants;
import com.company_management.common.enums.TaskStatusEnum;
import com.company_management.controller.auth.BaseController;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.projcet.RequestCreateTaskDTO;
import com.company_management.dto.request.projcet.RequestUpdateTaskDTO;
import com.company_management.dto.response.project.ResponseDetailTaskDTO;
import com.company_management.dto.response.project.ResponseListTaskDTO;
import com.company_management.dto.response.project.ResponseProjectDashboardTO;
import com.company_management.entity.Employee;
import com.company_management.entity.Project;
import com.company_management.entity.Task;
import com.company_management.exception.AppException;
import com.company_management.repository.EmployeeRepository;
import com.company_management.repository.ProjectRepository;
import com.company_management.repository.TaskRepository;
import com.company_management.service.EmployeeService;
import com.company_management.service.TaskService;
import com.company_management.utils.CommonUtils;
import com.company_management.utils.mapper.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl extends BaseController implements TaskService {
    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeService employeeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTask(RequestCreateTaskDTO request) {
        String userCode = getCurrentUserCode();
        Task task = new Task();
        String taskCodeMax = taskRepository.taskCodeMax();
        String taskCodeNext = CommonUtils.generateNextCode(taskCodeMax);
        MapperUtils.map(request, task);
        Employee employee = employeeService.getEmployee(request.getEmployeeCode());
        task.setEmployee(employee);
        task.setTaskCode(taskCodeNext);
        task.setManagerCode(userCode);
        Project project = projectRepository.findByProjectCode(request.getProjectCode())
                .orElseThrow(() -> new AppException("ER002", "Dự án không tồn tại trong hệ thống!"));
        task.setProject(project);
        task.setStatus(request.getTaskStatus());
        taskRepository.save(task);
    }

    @Override
    public ResponsePage<ResponseListTaskDTO> getTasks(TaskStatusEnum status, String keyword, RequestPage page) {
        keyword = CommonUtils.escapeLike(keyword);
        String userCode = getCurrentUserCode();
        Page<Task> taskPage;
        if (Constants.ADMIN.equalsIgnoreCase(userCode)) {
            taskPage = taskRepository.findByStatus(status.getCode(), keyword, page.toPageable());
        } else {
            taskPage = taskRepository.findByStatusV2(status.getCode(), userCode, keyword, page.toPageable());
        }

        List<ResponseListTaskDTO> data = getDataTask(taskPage);
        return new ResponsePage<>(data, page, taskPage.getTotalElements());
    }

    @Override
    public ResponsePage<ResponseListTaskDTO> getListPerson(TaskStatusEnum status, String keyword, RequestPage page) {
        String useCode = getCurrentUserCode();
        keyword = CommonUtils.escapeLike(keyword);
        Page<Task> taskPage;
        if (Constants.ADMIN.equalsIgnoreCase(useCode)) {
            taskPage = taskRepository.findByStatus(status.getCode(), keyword, page.toPageable());
        } else {
            taskPage = taskRepository.findByStatusAndEmployeeCode(status.getCode(), useCode, keyword, page.toPageable());
        }
        List<ResponseListTaskDTO> data = getDataTask(taskPage);
        return new ResponsePage<>(data, page, taskPage.getTotalElements());
    }


    @Override
    public List<ResponseProjectDashboardTO> getListDashboard() {
        List<Project> projects = projectRepository.findAll();
        List<ResponseProjectDashboardTO> data = new ArrayList<>();
        for (Project project : projects) {
            ResponseProjectDashboardTO dto = new ResponseProjectDashboardTO();
            dto.setProjectName(project.getProjectName());
            Object[] result = taskRepository.countTaskAndDoneByProjectCode(project.getProjectCode(), TaskStatusEnum.DONE.getCode());
            Object[] row = (Object[]) result[0];
            long tasksOfProject = Long.parseLong(row[0].toString());
            long taskDoneOfProject = Long.parseLong(row[1].toString());
            dto.setNumberOfTasks((int) tasksOfProject);
            dto.setNumberOfTasksDone((int) taskDoneOfProject);
            double percent = 0.0;
            if (taskDoneOfProject != 0 && tasksOfProject != 0) {
                percent = (double) taskDoneOfProject / tasksOfProject;
            }

            dto.setPercentage(percent);
            data.add(dto);
        }
        return data;
    }

    @Override
    public ResponseDetailTaskDTO getDetailTask(String code) {
        Task task = taskRepository.findByTaskCode(code).orElseThrow(() -> new AppException("ERR1", "Nhiệm vụ không tồn tại trong hệ thống!"));
        ResponseDetailTaskDTO dto = new ResponseDetailTaskDTO();
        MapperUtils.map(task, dto);
        Employee employee = task.getEmployee();
        if (employee != null) {
            dto.setEmployeeCode(employee.getCode());
        }
        Project project = task.getProject();
        if (project != null) {
            dto.setProjectCode(project.getProjectCode());
        }
        dto.setTaskStatus(task.getStatus());
        return dto;
    }

    @Override
    public void updateTask(RequestUpdateTaskDTO request) {
        Task task = taskRepository.findByTaskCode(request.getTaskCode()).orElseThrow(() -> new AppException("ERR01", "Không tìm thấy nhiệm vụ trong hệ thống"));
        MapperUtils.mapOnlyNotNullProperty(request, task);
        Employee employee = employeeRepository.findByCode(request.getEmployeeCode()).orElseThrow(() -> new AppException("ER01", "Nhân viên không tôn tại trong hệ thống"));
        task.setEmployee(employee);
        task.setStatus(request.getTaskStatus());
        taskRepository.save(task);
    }

    public List<ResponseListTaskDTO> getDataTask(Page<Task> taskPage) {
        return taskPage.getContent()
                .stream()
                .map(item -> {
                    ResponseListTaskDTO dto = new ResponseListTaskDTO();
                    Project project = item.getProject();
                    if (project != null) {
                        dto.setProjectName(project.getProjectName());
                    }
                    Employee employee = item.getEmployee();
                    if (employee != null) {
                        dto.setEmployeeName(employee.getFullName());
                    }
                    employeeRepository.findByCode(item.getManagerCode())
                            .ifPresent(empManager -> dto.setManagerName(empManager.getFullName()));
                    MapperUtils.map(item, dto);
                    return dto;
                }).toList();
    }


}

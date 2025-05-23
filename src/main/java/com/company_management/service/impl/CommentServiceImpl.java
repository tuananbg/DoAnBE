package com.company_management.service.impl;

import com.company_management.common.AuthConstants;
import com.company_management.common.Constants;
import com.company_management.common.enums.ConfigDataCode;
import com.company_management.dto.request.projcet.RequestCreateCommentDTO;
import com.company_management.dto.response.project.ResponseCommentDTO;
import com.company_management.entity.Comment;
import com.company_management.entity.Employee;
import com.company_management.entity.Task;
import com.company_management.repository.CommentRepository;
import com.company_management.repository.EmployeeRepository;
import com.company_management.repository.TaskRepository;
import com.company_management.service.CommentService;
import com.company_management.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final EmployeeService employeeService;

    @Override
    @Transactional
    public void createComment(RequestCreateCommentDTO request) {
        Comment comment = new Comment();
        Task task = taskRepository.findByTaskCode(request.getTaskCode()).orElse(null);
        if (task != null) {
            comment.setTask(task);
            comment.setTaskCode(request.getTaskCode());
        }
        Employee employee = employeeService.getEmployee(request.getEmployeeCode());
        comment.setEmployee(employee);
        comment.setContent(request.getContent());
        comment.setEmployeeCode(request.getEmployeeCode());
        commentRepository.save(comment);
    }

    @Override
    public List<ResponseCommentDTO> getCommentsByTaskCode(String taskCode) {
        List<Comment> comments = commentRepository.findByTaskCode(taskCode);
        List<ResponseCommentDTO> data = new ArrayList<>();
        for (Comment comment : comments) {
            ResponseCommentDTO response = new ResponseCommentDTO();
            response.setContent(comment.getContent());
            if (ConfigDataCode.ADMIN.equalsIgnoreCase(comment.getEmployeeCode())) {
                response.setEmployeeName(AuthConstants.ADMIN_NAME);
            }
            else {
                if (comment.getEmployee() != null) {
                    response.setEmployeeName(comment.getEmployee().getFullName());
                }
            }
            response.setCreateDate(comment.getCreatedDate());
            data.add(response);
        }
        return data;
    }
}

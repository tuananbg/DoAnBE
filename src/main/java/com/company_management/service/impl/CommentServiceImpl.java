package com.company_management.service.impl;

import com.company_management.common.enums.ConfigDataCode;
import com.company_management.dto.request.projcet.RequestCreateCommentDTO;
import com.company_management.dto.response.project.ResponseCommentDTO;
import com.company_management.entity.Comment;
import com.company_management.repository.CommentRepository;
import com.company_management.repository.EmployeeRepository;
import com.company_management.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EmployeeRepository employeeRepository;
    private static final String ADMIN_NAME="Quản trị hệ thống";
    @Override
    public void createComment(RequestCreateCommentDTO request) {
        Comment comment = new Comment();
        comment.setTaskCode(request.getTaskCode());
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
                response.setEmployeeName(ADMIN_NAME);
            }
            else {
                employeeRepository.findByCode(comment.getEmployeeCode()).ifPresent(employee -> response.setEmployeeName(employee.getFullName()));
            }
            response.setCreateDate(comment.getCreatedDate());
            data.add(response);
        }
        return data;
    }
}

package com.company_management.service;

import com.company_management.dto.request.projcet.RequestCreateCommentDTO;
import com.company_management.dto.response.project.ResponseCommentDTO;

import java.util.List;

public interface CommentService {

    void createComment(RequestCreateCommentDTO requestCreateCommentDTO);

    List<ResponseCommentDTO> getCommentsByTaskCode(String taskCode);
}

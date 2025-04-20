package com.company_management.controller.project;

import com.company_management.common.AppConstants;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.request.projcet.RequestCreateCommentDTO;
import com.company_management.dto.response.project.ResponseCommentDTO;
import com.company_management.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/comment")
@Slf4j
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping(value = "/create")
    public BaseResponse<Object> create(@RequestBody @Valid RequestCreateCommentDTO request) {
        commentService.createComment(request);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201, AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @GetMapping(value = "/list/{taskCode}")
    public BaseResponse<List<ResponseCommentDTO>> getList(@PathVariable("taskCode") String taskCode) {
        return BaseResponse.ok(commentService.getCommentsByTaskCode(taskCode));
    }
}

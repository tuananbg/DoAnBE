package com.company_management.dto.request.projcet;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestCreateCommentDTO {
    private String taskCode;
    private String content;
    private String employeeCode;
}

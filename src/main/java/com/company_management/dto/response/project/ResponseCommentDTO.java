package com.company_management.dto.response.project;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseCommentDTO {
    private String content;
    private String employeeName;
    private Date createDate;
}

package com.company_management.dto.response.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDetailListTaskDTO {
    private long id;
    private String taskName;
    private String taskStatusName;
    private String description;
}

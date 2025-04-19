package com.company_management.dto.response.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseListTaskOfProjectDTO {
    private String name;
    private List<ResponseDetailTaskDTO> taskForm;
}

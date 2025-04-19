package com.company_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMMENT") // bình luận
public class Comment extends BaseEntity{
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "EMPLOYEE_CODE")
    private String employeeCode;

    @Column(name = "TASK_CODE")
    private String taskCode;


}

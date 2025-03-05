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
@Table(name = "POSITION") // Chức danh
public class Position extends BaseEntity {

    @Column(name = "POSITION_CODE")
    private String positionCode;

    @Column(name = "POSITION_NAME")
    private String positionName;

    @Column(name = "POSITION_DESCRIPTION")
    private String positionDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department departmentId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "position_category_id")
    private PositionCategory positionCategory;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "job_group_id")
    private JobGroup jobGroup;

}

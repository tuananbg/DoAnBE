package com.company_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "seats")
@EntityListeners(AuditingEntityListener.class)
public class Seat extends BaseEntity {

    @Column(name = "CODE")
    private String code;

    //Id vị trí công việc của Quản lý trực tiếp
    @Column(name = "DIRECT_MANAGER")
    private Long directManager;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POSITION_ID")
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;


}
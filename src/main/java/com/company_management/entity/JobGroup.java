package com.company_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "JOB_GROUP") // Nhóm công việc
@EntityListeners(AuditingEntityListener.class)
public class JobGroup extends BaseEntity {

    @Column(name ="CODE")
    private String code; // Mã nhóm công việc

    @Column(name = "NAME")
    private String name; // Tên nhóm công việc

    @Column(name = "DESCRIPTION")
    private String description; // Mô tả

}

package com.company_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "USER_DETAIL_POSITION") // Tên bảng viết hoa
public class UserDetailPosition extends BaseEntity {

    @Column(name = "POSITION_ID")
    private Long positionId;

    @Column(name = "USER_DETAIL_ID")
    private Long userDetailId;
}

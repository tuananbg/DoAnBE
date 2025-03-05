package com.company_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "POSITION_CATEGORY") // Loại chức danh
@EntityListeners(AuditingEntityListener.class)
public class PositionCategory extends BaseEntity {

    @Column(name = "CODE")
    private String code; // Mã loại chức danh

    @Column(name = "NAME")
    private String name; // Loại chức danh

    @Column(name = "description")
    private String description; // Mô tả

    @OneToMany(mappedBy = "positionCategory", fetch = FetchType.LAZY)
    private List<Position> positions;

}
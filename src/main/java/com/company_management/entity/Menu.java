package com.company_management.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "menu")
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

//    @Schema(description = "Mã Menu")
    @Column(name = "CODE")
    private String code;

//    @Schema(description = "Tên Menu")
    @Column(name = "NAME")
    private String name;

//    @Schema(description = "icon class Menu")
    @Column(name = "ICON")
    private String icon;

//    @Schema(description = "Code menu cha")
    @Column(name = "PARENT_ID")
    private Long parentId;

//    @Schema(description = "Trạng thái hoạt động")
//    @Convert(converter = BooleanNumberConverter.class)
    @Column(name = "ACTIVE")
    private Boolean active;// Trạng thái kích hoạt: true = active, false = inactive

//    @Schema(description = "Sắp xếp theo thứ tự")
    @Column(name = "ORDER_BY")
    private Integer orderBy;

//    @Schema(description = "Loại menu list/process")
    @Column(name = "TYPE")
    private String type;

//    @Schema(description = "Url")
    @Column(name = "URL")
    private String url;
}

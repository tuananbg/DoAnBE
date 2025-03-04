package com.company_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MENU_ITEM")
public class MenuItem extends BaseEntity {

    @Column(name = "MENU_ITEM_CODE")
    private String menuItemCode;

    @Column(name = "MENU_ITEM_NAME")
    private String menuItemName;

}

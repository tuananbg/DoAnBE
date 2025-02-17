package com.company_management.service;

import com.company_management.dto.MenuItemDTO;
import com.company_management.dto.response.BasicResponse;

import java.util.List;

public interface MenuItemService {
    BasicResponse addMenuItem(MenuItemDTO menuItemDTO);

    BasicResponse updateMenuItem(MenuItemDTO menuItemDTO);

    List<MenuItemDTO> getAll();
}

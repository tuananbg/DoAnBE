package com.company_management.dto.mapper;

import com.company_management.dto.MenuItemDTO;
import com.company_management.entity.MenuItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface MenuItemMapper extends EntityMapper<MenuItem, MenuItemDTO> {
}

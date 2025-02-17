package com.company_management.dto.mapper;

import com.company_management.entity.Role;
import com.company_management.dto.RoleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper extends EntityMapper<Role, RoleDTO>{
}

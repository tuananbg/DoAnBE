package com.company_management.dto.mapper;

import com.company_management.dto.DepartmentDTO;
import com.company_management.entity.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface DepartmentMapper extends EntityMapper<Department, DepartmentDTO> {
}

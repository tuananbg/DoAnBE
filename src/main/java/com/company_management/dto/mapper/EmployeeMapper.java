package com.company_management.dto.mapper;

import com.company_management.dto.UserDetailDTO;
import com.company_management.entity.UserDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface EmployeeMapper extends EntityMapper<UserDetail, UserDetailDTO> {
}

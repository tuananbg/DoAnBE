package com.company_management.dto.mapper;

import com.company_management.dto.UserCustomDTO;
import com.company_management.entity.UserCustom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface UserCustomMapper extends EntityMapper<UserCustom, UserCustomDTO> {
}

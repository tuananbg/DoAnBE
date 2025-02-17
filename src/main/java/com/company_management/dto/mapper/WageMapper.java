package com.company_management.dto.mapper;

import com.company_management.dto.WageDTO;
import com.company_management.entity.Wage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface WageMapper extends EntityMapper<Wage, WageDTO> {
}

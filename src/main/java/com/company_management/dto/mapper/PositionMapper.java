package com.company_management.dto.mapper;

import com.company_management.dto.PositionDTO;
import com.company_management.entity.Position;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface PositionMapper extends EntityMapper<Position, PositionDTO> {
}

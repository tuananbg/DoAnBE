package com.company_management.dto.mapper;

import com.company_management.dto.AttendanceOTDTO;
import com.company_management.entity.AttendanceOt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AttendanceOTMapper extends EntityMapper<AttendanceOt, AttendanceOTDTO> {
}

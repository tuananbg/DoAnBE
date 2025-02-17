package com.company_management.dto.mapper;

import com.company_management.dto.AttendanceLeaveDTO;
import com.company_management.entity.AttendanceLeave;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface AttendanceLeaveMapper extends EntityMapper<AttendanceLeave, AttendanceLeaveDTO> {
}

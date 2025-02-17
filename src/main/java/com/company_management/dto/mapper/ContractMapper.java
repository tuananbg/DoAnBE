package com.company_management.dto.mapper;

import com.company_management.dto.ContractDTO;
import com.company_management.entity.Contract;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ContractMapper extends EntityMapper<Contract, ContractDTO> {
}

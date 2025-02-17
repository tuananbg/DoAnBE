package com.company_management.dto.mapper;

import com.company_management.dto.SocialInsuranceDTO;
import com.company_management.entity.SocialInsurance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface SocialInsuranceMapper extends EntityMapper<SocialInsurance, SocialInsuranceDTO>{
}

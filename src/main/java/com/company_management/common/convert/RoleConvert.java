package com.company_management.common.convert;

import com.company_management.common.enums.RoleEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter(autoApply = true)
public class RoleConvert implements AttributeConverter<RoleEnum, String> {
    @Override
    public String convertToDatabaseColumn(RoleEnum attribute) {
        try {
            return attribute == null ? null : attribute.getCode();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public RoleEnum convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : RoleEnum.findByCode(dbData);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return null;
        }
    }

}

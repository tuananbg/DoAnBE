package com.company_management.common.convert;

import com.company_management.common.enums.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter(autoApply = true)
public class RoleConvert implements AttributeConverter<Role, String> {
    @Override
    public String convertToDatabaseColumn(Role attribute) {
        try {
            return attribute == null ? null : attribute.getCode();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : Role.findByCode(dbData);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return null;
        }
    }

}

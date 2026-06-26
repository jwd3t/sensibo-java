package com.sensibo.platform.pc26010u202318609.registry.infrastructure.persistence.jpa.converters;

import com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects.SerialNumber;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SerialNumberPersistenceConverter implements AttributeConverter<SerialNumber, String> {

    @Override
    public String convertToDatabaseColumn(SerialNumber attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public SerialNumber convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new SerialNumber(dbData);
    }
}

package com.sensibo.platform.pc26010u202318609.registry.infrastructure.persistence.jpa.converters;

import com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects.SensiboUserId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SensiboUserIdPersistenceConverter implements AttributeConverter<SensiboUserId, Long> {

    @Override
    public Long convertToDatabaseColumn(SensiboUserId attribute) {
        return attribute == null ? null : attribute.userId();
    }

    @Override
    public SensiboUserId convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : new SensiboUserId(dbData);
    }
}

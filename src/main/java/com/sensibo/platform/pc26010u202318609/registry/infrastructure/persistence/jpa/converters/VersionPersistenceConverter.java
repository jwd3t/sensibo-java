package com.sensibo.platform.pc26010u202318609.registry.infrastructure.persistence.jpa.converters;

import com.sensibo.platform.pc26010u202318609.registry.domain.model.valueobjects.Version;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VersionPersistenceConverter implements AttributeConverter<Version, String> {
    @Override
    public String convertToDatabaseColumn(Version attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public Version convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Version(dbData);
    }
}

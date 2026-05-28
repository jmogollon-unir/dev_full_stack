package com.relatos_papel.catalogue.domain.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookFormatConverter implements AttributeConverter<BookFormat, String> {

    @Override
    public String convertToDatabaseColumn(BookFormat attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public BookFormat convertToEntityAttribute(String dbData) {
        return BookFormat.fromValue(dbData);
    }
}
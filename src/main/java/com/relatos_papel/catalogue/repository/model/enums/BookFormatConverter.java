package com.relatos_papel.catalogue.repository.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookFormatConverter implements AttributeConverter<BookFormat, String> {

    @Override
    public String convertToDatabaseColumn(BookFormat attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue(); // Guarda "Físico", "Digital", o "Físico y Digital"
    }

    @Override
    public BookFormat convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return BookFormat.fromValue(dbData);
    }
}
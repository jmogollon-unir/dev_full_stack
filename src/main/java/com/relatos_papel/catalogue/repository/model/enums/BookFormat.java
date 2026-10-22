package com.relatos_papel.catalogue.repository.model.enums;

import lombok.Getter;

@Getter
public enum BookFormat {
    FISICO("Físico"),
    DIGITAL("Digital"),
    AMBOS("Físico y Digital");

    private final String value;

    BookFormat(String value) {
        this.value = value;
    }

    public static BookFormat fromValue(String value) {
        if (value == null) return null;
        for (BookFormat format : values()) {
            if (format.value.equalsIgnoreCase(value) || format.name().equalsIgnoreCase(value)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Formato desconocido: " + value);
    }
}

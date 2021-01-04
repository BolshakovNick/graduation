package ru.bolshakov.internship.dishes_rating.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class FieldErrorDTO {
    @Schema(description = "Name of field")
    private final String fieldName;

    @Schema(description = "Message which describes why field was rejected")
    private final String message;

    public FieldErrorDTO(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }
}

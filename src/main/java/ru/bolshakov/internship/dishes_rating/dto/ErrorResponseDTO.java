package ru.bolshakov.internship.dishes_rating.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO {
    @Schema(description = "Exception message")
    private String message;

    private List<FieldErrorDTO> fieldsErrors;

    public ErrorResponseDTO() {
    }

    public ErrorResponseDTO(String message) {
        this(message, null);
    }

    public ErrorResponseDTO(List<FieldErrorDTO> fieldErrors) {
        this(null, fieldErrors);
    }

    public ErrorResponseDTO(String message, List<FieldErrorDTO> fieldsErrors) {
        this.message = message;
        this.fieldsErrors = fieldsErrors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldErrorDTO> getFieldsErrors() {
        return fieldsErrors;
    }

    public void setFieldsErrors(List<FieldErrorDTO> fieldsErrors) {
        this.fieldsErrors = fieldsErrors;
    }
}

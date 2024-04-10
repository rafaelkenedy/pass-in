package com.rafael.passin.dto.errors;

import java.util.List;

public record ErrorResponse(List<FieldErrorDTO> fieldErrors, List<GlobalErrorDTO> globalErrors) {
}

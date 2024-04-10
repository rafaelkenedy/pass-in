package com.rafael.passin.dto.event;

import jakarta.validation.constraints.NotBlank;

public record EventRequestDTO(
        @NotBlank
        String title,
        @NotBlank
        String details,
        Integer maximumAttendees
) {
}

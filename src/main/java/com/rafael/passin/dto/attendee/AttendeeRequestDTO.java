package com.rafael.passin.dto.attendee;

import jakarta.validation.constraints.NotBlank;

public record AttendeeRequestDTO(
        @NotBlank
        String name,
        @NotBlank
        String email
) {
}

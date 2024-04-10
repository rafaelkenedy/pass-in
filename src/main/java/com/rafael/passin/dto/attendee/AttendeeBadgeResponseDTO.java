package com.rafael.passin.dto.attendee;

import java.net.URI;

public record AttendeeBadgeResponseDTO(
        String name,
        String email,
        String eventId,
        URI checkInUrl
) {
}

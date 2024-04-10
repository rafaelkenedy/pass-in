package com.rafael.passin.services;

import com.rafael.passin.domain.attendee.Attendee;
import com.rafael.passin.domain.checkin.CheckIn;
import com.rafael.passin.domain.exceptions.ResourceNotFoundException;
import com.rafael.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.rafael.passin.dto.attendee.AttendeeDetails;
import com.rafael.passin.dto.attendee.AttendeesListResponseDTO;
import com.rafael.passin.repositories.AttendeeRepository;
import com.rafael.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final CheckInRepository checkInRepository;

    public List<Attendee> getAllAttendeesFromEvent(String eventId) {

        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId) {
        List<Attendee> attendees = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendees.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInRepository.findByAttendeeId(attendee.getId());
            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
            return new AttendeeDetails(
                    attendee.getId(),
                    attendee.getName(),
                    attendee.getEmail(),
                    attendee.getCreatedAt(),
                    checkedInAt
            );
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder builder) {
        var attendee = getAttendee(attendeeId);

        var uri = builder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri();

        return new AttendeeBadgeResponseDTO(
                attendee.getName(),
                attendee.getEmail(),
                attendee.getEvent().getId(),
                uri
        );
    }

    public Attendee getAttendee(String attendeeId) {
        return this.attendeeRepository.findById(attendeeId).orElseThrow(
                () -> new ResourceNotFoundException("Attendee with id " + attendeeId + " not found")
        );
    }
}

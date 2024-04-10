package com.rafael.passin.controllers;

import com.rafael.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.rafael.passin.services.AttendeeService;
import com.rafael.passin.services.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class AttendeeController {

    private final AttendeeService attendeeService;
    private final CheckInService checkInService;

    @GetMapping("/{attendeeId}/badge")
    public ResponseEntity<AttendeeBadgeResponseDTO> getAttendeeBadge(@PathVariable String attendeeId, UriComponentsBuilder builder) {

        var response = this.attendeeService.getAttendeeBadge(attendeeId, builder);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{attendeeId}/check-in")
    public ResponseEntity<?> registerCheckIn(@PathVariable String attendeeId, UriComponentsBuilder builder) {
        this.checkInService.registerCheckIn(attendeeService.getAttendee(attendeeId));
        var uri = builder.path("/attendees/{attendId}/badge").buildAndExpand(attendeeId).toUri();
        return ResponseEntity.created(uri).build();
    }

}

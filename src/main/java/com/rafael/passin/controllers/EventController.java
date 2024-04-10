package com.rafael.passin.controllers;


import com.rafael.passin.dto.attendee.AttendeesListResponseDTO;
import com.rafael.passin.dto.event.EventIdDTO;
import com.rafael.passin.dto.event.EventRequestDTO;
import com.rafael.passin.dto.event.EventResponseDTO;
import com.rafael.passin.services.AttendeeService;
import com.rafael.passin.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping({"/{id}"})
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id) {
        EventResponseDTO event = this.eventService.getEventDetail(id);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder builder) {
        EventIdDTO eventIdDTO = this.eventService.createEvent(body);

        var uri = builder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();

        return ResponseEntity.created(uri).body(eventIdDTO);
    }

    @GetMapping({"attendees/{id}"})
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String id) {
        AttendeesListResponseDTO attendees = this.attendeeService.getEventsAttendee(id);
        return ResponseEntity.ok(attendees);
    }
}

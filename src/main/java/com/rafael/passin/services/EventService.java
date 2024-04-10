package com.rafael.passin.services;


import com.rafael.passin.domain.attendee.Attendee;
import com.rafael.passin.domain.event.Event;
import com.rafael.passin.domain.exceptions.AlreadyExistsException;
import com.rafael.passin.domain.exceptions.EventCapacityExceededException;
import com.rafael.passin.domain.exceptions.ResourceNotFoundException;
import com.rafael.passin.dto.attendee.AttendeeIdDTO;
import com.rafael.passin.dto.attendee.AttendeeRequestDTO;
import com.rafael.passin.dto.event.EventIdDTO;
import com.rafael.passin.dto.event.EventRequestDTO;
import com.rafael.passin.dto.event.EventResponseDTO;
import com.rafael.passin.repositories.AttendeeRepository;
import com.rafael.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.LimitExceededException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;

    public EventResponseDTO getEventDetail(String eventId) {
        var event = this.getEvent(eventId);
        List<Attendee> attendeeList = getAttendees(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventDTO) {
        Event newEvent = new Event();

        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(createSlug(eventDTO.title()));

        this.eventRepository.save(newEvent);
        return new EventIdDTO(newEvent.getId());
    }

    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeDTO) {
        this.attendeeRepository.findByEventIdAndEmail(eventId, attendeeDTO.email()).ifPresent(attendee -> {
            throw new AlreadyExistsException("Attendee with email " + attendeeDTO.email() + " already registered for event " + eventId);
        });

        var event = this.getEvent(eventId);

        List<Attendee> attendeeList = getAttendees(eventId);
        if (event.getMaximumAttendees() <= attendeeList.size()) {
            throw new EventCapacityExceededException("Event with id " + eventId + " has reached maximum capacity");
        }

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeDTO.name());
        newAttendee.setEmail(attendeeDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.attendeeRepository.save(newAttendee);

        return new AttendeeIdDTO(newAttendee.getId());
    }

    private List<Attendee> getAttendees(String eventId) {
        List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);
        return attendeeList;
    }

    private Event getEvent(String eventId) {
        return this.eventRepository.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event with id " + eventId + " not found"));
    }

    private String createSlug(String text) {
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalizedText.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replaceAll("[^\\w\\s]", "").replaceAll("\\s+", "-").toLowerCase();
    }

}

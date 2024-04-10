package com.rafael.passin.services;


import com.rafael.passin.domain.attendee.Attendee;
import com.rafael.passin.domain.event.Event;
import com.rafael.passin.domain.exceptions.ResourceNotFoundException;
import com.rafael.passin.dto.event.EventIdDTO;
import com.rafael.passin.dto.event.EventRequestDTO;
import com.rafael.passin.dto.event.EventResponseDTO;
import com.rafael.passin.repositories.AttendeeRepository;
import com.rafael.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;

    public EventResponseDTO getEventDetail(String eventId) {
        var event = this.eventRepository.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event with id " + eventId + " not found"));
        List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);
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

    private String createSlug(String text) {
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalizedText
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }

}

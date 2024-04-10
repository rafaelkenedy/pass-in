package com.rafael.passin.services;

import com.rafael.passin.domain.attendee.Attendee;
import com.rafael.passin.domain.checkin.CheckIn;
import com.rafael.passin.domain.exceptions.AlreadyExistsException;
import com.rafael.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {

    private final CheckInRepository checkInRepository;


    public void registerCheckIn(Attendee attendee) {
        this.verifyCheckInExists(attendee.getId());
        CheckIn checkIn = new CheckIn();
        checkIn.setAttendee(attendee);
        checkIn.setCreatedAt(LocalDateTime.now());
        this.checkInRepository.save(checkIn);
    }

    private void verifyCheckInExists(String id) {
        this.checkInRepository.findByAttendeeId(id).ifPresent(checkIn -> {
            throw new AlreadyExistsException("Attendee already checked in");
        });
    }

}

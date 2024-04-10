package com.rafael.passin.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendees")
public class AttendeeController {

    @GetMapping
    public ResponseEntity<String> getAttendees() {
        return ResponseEntity.ok("Attendees");
    }
}

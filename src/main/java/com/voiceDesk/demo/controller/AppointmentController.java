package com.voiceDesk.demo.controller;

import com.voiceDesk.demo.model.Appointment;
import com.voiceDesk.demo.repository.AppointmentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentRepository repo;

    public AppointmentController(AppointmentRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Appointment book(@RequestBody Appointment appointment) {
        return repo.save(appointment);
    }

    @GetMapping
    public List<Appointment> getAll() {
        return repo.findAll();
    }
}
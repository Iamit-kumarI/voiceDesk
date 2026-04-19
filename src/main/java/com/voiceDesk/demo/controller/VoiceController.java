package com.voiceDesk.demo.controller;

import com.voiceDesk.demo.entity.Appointment;
import com.voiceDesk.demo.repository.AppointmentRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoiceController {

    private final AppointmentRepository appointmentRepository;

    public VoiceController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @PostMapping(value = "/voice", produces = "application/xml")
    public String voice() {
        return """
        <Response>
            <Gather input="speech" 
            action="https://0d11-103-175-180-89.ngrok-free.app/process" 
            method="POST">
                <Say>Hello! Please tell me your name and appointment details.</Say>
            </Gather>
        </Response>
        """;
    }
    @PostMapping(value = "/process", produces = "application/xml")
    public String process(@RequestParam("SpeechResult") String speech) {

        System.out.println("User said: " + speech);

        Appointment appointment = new Appointment();
        appointment.setMessage(speech);

//        appointmentRepository.save(appointment);
        appointmentRepository.save(appointment);

        return """
        <Response>
            <Say>Thank you! Your appointment is recorded.</Say>
        </Response>
        """;
    }
}
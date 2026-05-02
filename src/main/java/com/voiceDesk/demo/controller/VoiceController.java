package com.voiceDesk.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voiceDesk.demo.entity.Appointment;
import com.voiceDesk.demo.model.AppointmentDto;
import com.voiceDesk.demo.repository.AppointmentDtoRepository;
import com.voiceDesk.demo.repository.AppointmentRepository;
import com.voiceDesk.demo.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoiceController {

    private final AppointmentRepository appointmentRepository;

//    public VoiceController(AppointmentRepository appointmentRepository) {
//        this.appointmentRepository = appointmentRepository;
//    }
    @PostMapping(value = "/voice", produces = "application/xml")
    public String voice() {
        return """
        <Response>
            <Gather input="speech" 
            action="https://254a-103-94-67-27.ngrok-free.app/process" 
            method="POST">
                <Say>Hello! Please tell me your name, And Meeting Details/Say>
            </Gather>
        </Response>
        """;
    }
    public VoiceController(
            AppointmentRepository appointmentRepository,
            AppointmentDtoRepository dtoRepo,
            ObjectMapper objectMapper,
            AiService aiService
    ) {
        this.appointmentRepository = appointmentRepository;
        this.dtoRepo = dtoRepo;
        this.objectMapper = objectMapper;
        this.aiService = aiService;
    }
    @Autowired
    private AppointmentRepository repo;

    @Autowired
    private AppointmentDtoRepository dtoRepo;

    @Autowired
    private AiService aiService;

    @Autowired
    private ObjectMapper objectMapper;
    @PostMapping(value = "/process", produces = "application/xml")
    public String process(@RequestParam("SpeechResult") String speech) {

        try {
            System.out.println("User said: " + speech);

            // 1. Save raw message
            Appointment appointment = new Appointment();
            appointment.setMessage(speech);
            appointmentRepository.save(appointment);

            // 2. Call AI
            String aiResponse = aiService.extractDetails(speech);

            JsonNode root = objectMapper.readTree(aiResponse);
            String jsonText = root.get("response").asText();

            AppointmentDto dto = objectMapper.readValue(jsonText, AppointmentDto.class);
            dtoRepo.save(dto);

        } catch (Exception e) {
            e.printStackTrace();

            // IMPORTANT: Always return valid XML even on error
            return """
        <Response>
            <Say>Sorry, something went wrong. Please try again.</Say>
        </Response>
        """;
        }

        return """
    <Response>
        <Say>Thank you! Your appointment is recorded.</Say>
    </Response>
    """;
    }
}
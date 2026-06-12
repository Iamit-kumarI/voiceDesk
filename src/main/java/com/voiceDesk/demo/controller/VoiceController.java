package com.voiceDesk.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voiceDesk.demo.entity.Appointment;
import com.voiceDesk.demo.model.AppointmentDto;
import com.voiceDesk.demo.repository.AppointmentDtoRepository;
import com.voiceDesk.demo.repository.AppointmentRepository;
import com.voiceDesk.demo.service.AiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoiceController {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentDtoRepository dtoRepo;
    private final ObjectMapper objectMapper;
    private final AiService aiService;

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

    @PostMapping(value = "/voice", produces = "application/xml")
    public String voice() {
        return """
        <Response>
            <Gather input="speech"
            action="https://8928-151-158-52-167.ngrok-free.app/process"
            method="POST"
            timeout="10">
                <Say>Hello! Please tell me your name and meeting details.</Say>
            </Gather>
            <Say>We did not receive any input. Goodbye.</Say>
        </Response>
        """;
    }

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
            System.out.println("AI raw response: " + aiResponse);

            // 3. Parse Ollama wrapper: {"response": "...json..."}
            JsonNode root = objectMapper.readTree(aiResponse);
            String jsonText = root.get("response").asText();

            // 4. Strip markdown code fences if llama3 added them
            jsonText = jsonText.trim();
            if (jsonText.startsWith("```")) {
                jsonText = jsonText.replaceAll("(?s)^```[a-zA-Z]*\\n?", "").replaceAll("```$", "").trim();
            }

            // 5. Extract just the JSON object in case there's extra text around it
            int start = jsonText.indexOf('{');
            int end = jsonText.lastIndexOf('}');
            if (start != -1 && end != -1) {
                jsonText = jsonText.substring(start, end + 1);
            }

            System.out.println("Parsed JSON: " + jsonText);

            AppointmentDto dto = objectMapper.readValue(jsonText, AppointmentDto.class);
            dtoRepo.save(dto);

        } catch (Exception e) {
            e.printStackTrace();

            return """
        <Response>
            <Say>Sorry, something went wrong. Please try again.</Say>
        </Response>
        """;
        }

        return """
    <Response>
        <Say>Thank you! Your appointment has been recorded.</Say>
    </Response>
    """;
    }
}

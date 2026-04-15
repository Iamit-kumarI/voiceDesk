package com.voiceDesk.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoiceController {

    @PostMapping(value = "/voice", produces = "application/xml")
    public String voice() {
        return """
            <Response>
                <Say>Jay Shree Ram Bhai, 
                Jo Ram Ko Laye Hai Hum Unko Layenge
                </Say>
            </Response>
            """;
    }
}
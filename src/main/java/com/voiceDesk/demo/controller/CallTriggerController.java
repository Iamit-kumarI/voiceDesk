package com.voiceDesk.demo.controller;

import com.voiceDesk.demo.service.CallService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/call")
public class CallTriggerController {

    private final CallService service;

    public CallTriggerController(CallService service) {
        this.service = service;
    }

    @GetMapping
    public String call(@RequestParam String number) {
        service.makeCall(number);
        return "Calling...";
    }
}
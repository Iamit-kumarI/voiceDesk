package com.voiceDesk.demo.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class CallService {

    private static final Dotenv dotenv = Dotenv.load();

    public static final String ACCOUNT_SID = dotenv.get("TWILIO_SID");
    public static final String AUTH_TOKEN = dotenv.get("TWILIO_AUTH");
    public static final String FROM_NUMBER = "+12185042301";

    // Read ngrok URL from .env so you don't need to recompile when it changes
    public static final String NGROK_URL = dotenv.get("NGROK_URL");

    @PostConstruct
    public void init() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void makeCall(String toNumber) {
        Call.creator(
                new PhoneNumber(toNumber),
                new PhoneNumber(FROM_NUMBER),
                URI.create(NGROK_URL + "/voice")
        ).create();
    }
}

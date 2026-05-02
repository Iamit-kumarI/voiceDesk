package com.voiceDesk.demo.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class CallService {

    private static final Dotenv dotenv = Dotenv.load();

    public static final String ACCOUNT_SID = dotenv.get("TWILIO_SID");
    public static final String AUTH_TOKEN = dotenv.get("TWILIO_AUTH");
    public static final String FROM_NUMBER = "+12185042301";

    public void makeCall(String toNumber) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Call.creator(
                new PhoneNumber(toNumber),
                new PhoneNumber(FROM_NUMBER),
                URI.create("https://53a2-103-175-180-33.ngrok-free.app/voice")
        ).create();
    }
}
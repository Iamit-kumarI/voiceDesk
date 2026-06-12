package com.voiceDesk.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class AiService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String extractDetails(String message) {
        try {
            URL url = new URL("http://localhost:11434/api/generate");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String prompt = """
                Extract details from this message and return ONLY a raw JSON object, no explanation, no markdown:
                message: "%s"

                Return exactly this format:
                {
                  "customerName": "",
                  "phoneNumber": "",
                  "date": "",
                  "time": ""
                }
                """.formatted(message.replace("\"", "\\\""));

            // Use Jackson to safely build the JSON body — no manual string escaping
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "llama3");
            requestBody.put("prompt", prompt);
            requestBody.put("stream", false);
            String body = objectMapper.writeValueAsString(requestBody);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

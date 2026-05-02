package com.voiceDesk.demo.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class AiService {

    public String extractDetails(String message) {
        try {
            URL url = new URL("http://localhost:11434/api/generate");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String prompt = """
                Extract details from this message and return JSON only:
                message: "%s"

                format:
                {
                  "name": "",
                  "phone": "",
                  "date": "",
                  "time": ""
                }
                """.formatted(message);

            String body = """
                {
                  "model": "llama3",
                  "prompt": %s,
                  "stream": false
                }
                """.formatted("\"" + prompt.replace("\"", "\\\"") + "\"");

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
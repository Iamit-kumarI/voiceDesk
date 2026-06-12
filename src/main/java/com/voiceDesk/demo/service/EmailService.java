package com.voiceDesk.demo.service;

import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {
    public void SendEmail(String toEmail){
        Properties prop=new Properties();
        prop.put("mail.smtp.auth","true");
        prop.put("mail.smtp.starttls.enable","true");
        prop.put("mail.smtp.host","smtp.gmail.com");
        prop.put("mail.smtp.port","587");

        //start a new session
    }
}

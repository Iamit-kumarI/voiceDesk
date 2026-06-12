package com.voiceDesk.demo.service;

import com.voiceDesk.demo.config.EnvConfig;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
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
        Session session=Session.getInstance(
                prop,new Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(
                                EnvConfig.GmailUserName,
                                EnvConfig.GmailAppPass
                        );
                    }
        });
    }
}

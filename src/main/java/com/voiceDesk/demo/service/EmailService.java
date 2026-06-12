package com.voiceDesk.demo.service;

import com.voiceDesk.demo.config.EnvConfig;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
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
        try{
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(EnvConfig.GmailUserName));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toEmail));
            message.setSubject("Appointment Confirmed");
            message.setText("Your Appointment Has been confirmed");
            Transport.send(message);
            System.out.println("Email Send Successfully");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

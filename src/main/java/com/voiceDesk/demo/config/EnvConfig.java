package com.voiceDesk.demo.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    public static final Dotenv dotenv=Dotenv.load();

    public static final String GmailUserName=dotenv.get("GmailUserName");
    public static final String GmailAppPass=dotenv.get("GmailAppPass");

}

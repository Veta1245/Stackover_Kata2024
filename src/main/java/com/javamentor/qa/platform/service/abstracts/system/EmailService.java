package com.javamentor.qa.platform.service.abstracts.system;

public interface EmailService {
    boolean sendMessage(String to, String messageText, String subject);
}
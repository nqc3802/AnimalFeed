package com.example.animal_feed.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nqc3822@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        try {
            javaMailSender.send(message);
            System.out.println("Email sent successfully");
        } catch (MailException e) {
            System.out.println("Failed to send email: " + e.getMessage());
            throw e;
        }
    }
}

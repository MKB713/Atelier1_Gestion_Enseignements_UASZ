package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired(required = false)
    private JavaMailSender javaMailSender;

    public void sendMail(String to, String subject, String text) {
        if (javaMailSender == null) {
            System.out.println("Configuration mail non disponible. Message non envoyé.");
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            javaMailSender.send(message);
            System.out.println("Message envoyé avec succès !");
        } catch (Exception e) {
            System.out.println("Échec de l'envoi du message : " + e.getMessage());
        }


    }
}

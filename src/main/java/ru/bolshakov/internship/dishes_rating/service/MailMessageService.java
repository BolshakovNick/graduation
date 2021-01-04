package ru.bolshakov.internship.dishes_rating.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.bolshakov.internship.dishes_rating.exception.MessageSendingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailMessageService {
    private final ExecutorService executorService;

    private final JavaMailSender mailSender;

    public MailMessageService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.executorService = Executors.newCachedThreadPool();
    }

    public void sendLetterAsynchronously(String email, String subject, String messageText) {
        executorService.execute(() -> sendLetter(email, subject, messageText));
    }

    public void sendLetter(String email, String subject, String messageText) {
        mailSender.send(constructMessage(email, subject, messageText));
    }

    private MimeMessage constructMessage(String email, String subject, String messageText) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(messageText, true);
            return message;
        } catch (MessagingException e) {
            throw new MessageSendingException("Exception while creating MimeMessage", e);
        }
    }
}

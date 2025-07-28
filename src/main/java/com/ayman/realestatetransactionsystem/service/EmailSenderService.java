package com.ayman.realestatetransactionsystem.service;

import com.ayman.realestatetransactionsystem.model.Notification;
import com.ayman.realestatetransactionsystem.model.Transaction;
import com.ayman.realestatetransactionsystem.model.User;
import com.ayman.realestatetransactionsystem.model.dto.EmailStruct;
import com.ayman.realestatetransactionsystem.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class EmailSenderService {
    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;


    @Value("${EMAIL}")
    private String fromEmail;


    @RabbitListener(queues = {"${rabbitmq-json-queue-name}"})
    private void listener(EmailStruct emailStruct) {
        createNotificationAndSendEmail(emailStruct.getReceiver(), emailStruct.getSubject(), emailStruct.getBody());
    }

    private void createNotificationAndSendEmail(User receiver, String subject, String body) {
        Notification notification = Notification.builder()
                .message(body)
                .date(LocalDateTime.now())
                .receiver(receiver)
                .build();
        notificationRepository.save(notification);

        try {
            sendEmail(receiver.getEmail(), subject, body);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmail(String toEmail, String subject, String body) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject(subject);

        // Load HTML template from resources
        ClassPathResource resource = new ClassPathResource("templates/email-template.html");
        String htmlTemplate;
        try (InputStream inputStream = resource.getInputStream()) {
            htmlTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        // Replace placeholders
        String htmlContent = htmlTemplate
                .replace("{{body}}", body);

        helper.setText(htmlContent, true); // true = HTML content

        // Attach inline image
        ClassPathResource logoImage = new ClassPathResource("templates/brand-inspire-logo-white.png");
        helper.addInline("brandLogo", logoImage);

        mailSender.send(message);
        log.info("Email sent to {}", toEmail);
    }


}

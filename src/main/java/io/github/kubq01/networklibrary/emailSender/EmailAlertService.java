package io.github.kubq01.networklibrary.emailSender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailAlertService {

    private final JavaMailSender mailSender;

    @Value("${network.security.alerts.email.enabled:true}")
    private boolean emailAlertsEnabled;

    @Value("${network.security.alerts.email.recipient:admin@example.com}")
    private String recipientEmail;

    @Value("${network.security.alerts.email.subject:Network Security Alert}")
    private String emailSubject;

    public EmailAlertService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAlert(String message) {
        if (!emailAlertsEnabled) {
            log.info("[EmailAlert] Wysyłanie e-maili jest wyłączone.");
            return;
        }

        log.warn("[ALERT] Wykryto zagrożenie: {}", message);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject(emailSubject);
        mailMessage.setText(message);

        try {
            mailSender.send(mailMessage);
            log.info("[EmailAlert] Wysłano alert na adres: {}", recipientEmail);
        } catch (Exception e) {
            log.error("[EmailAlert] Błąd wysyłania e-maila: {}", e.getMessage());
        }
    }
}

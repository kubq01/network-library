package io.github.kubq01.networklibrary.emailSender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

class EmailAlertServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailAlertService emailAlertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(emailAlertService, "emailAlertsEnabled", true);
        ReflectionTestUtils.setField(emailAlertService, "recipientEmail", "security@example.com");
        ReflectionTestUtils.setField(emailAlertService, "emailSubject", "Security Alert");
    }

    @Test
    void shouldSendEmailAlert() {
        String alertMessage = "Test Security Alert";

        emailAlertService.sendAlert(alertMessage);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void shouldNotSendEmailWhenDisabled() {
        ReflectionTestUtils.setField(emailAlertService, "emailAlertsEnabled", false);

        emailAlertService.sendAlert("Test Message");

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }
}

package io.github.kubq01.networklibrary.filter;

import io.github.kubq01.networklibrary.emailSender.EmailAlertService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.Mockito.*;

class BruteForceFilterTest {

    private BruteForceFilter filter;
    private HttpServletRequest request;
    private ServletResponse response;
    private FilterChain chain;

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
        filter = new BruteForceFilter(emailAlertService);
        request = mock(HttpServletRequest.class);
        response = mock(ServletResponse.class);
        chain = mock(FilterChain.class);
    }

    @Test
    void shouldDetectBruteForce() throws IOException, jakarta.servlet.ServletException {
        when(request.getRemoteAddr()).thenReturn("192.168.1.2");
        when(request.getRequestURI()).thenReturn("/login");

        for (int i = 0; i < 35; i++) { // Exceed limit
            filter.doFilter(request, response, chain);
        }

        verify(mailSender, times(5)).send(any(SimpleMailMessage.class));
        verify(chain, times(35)).doFilter(request, response);
    }
}
package io.github.kubq01.networklibrary.filter;

import io.github.kubq01.networklibrary.emailSender.EmailAlertService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@Order(2)
public class BruteForceFilter implements Filter {

    private static final Map<String, AtomicInteger> loginAttempts = new ConcurrentHashMap<>();
    private static final int ATTEMPT_LIMIT = 30;
    private static final long LOCKOUT_TIME_MS = 300_000;

    private final EmailAlertService emailAlertService;

    public BruteForceFilter(EmailAlertService emailAlertService) {
        this.emailAlertService = emailAlertService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest) {
            String clientIp = request.getRemoteAddr();
            String path = httpRequest.getRequestURI();

            if (path.contains("/login")) {
                loginAttempts.putIfAbsent(clientIp, new AtomicInteger(0));
                int attempts = loginAttempts.get(clientIp).incrementAndGet();

                log.info("[Brute Force Protection] IP: {} - Próba logowania nr {}", clientIp, attempts);

                if (attempts > ATTEMPT_LIMIT) {
                    log.warn("[Brute Force Alert] Podejrzane próby logowania z IP: {}", clientIp);
                    emailAlertService.sendAlert("Podejrzany ruch z IP: " + clientIp + ". Podejrzenie ataku Brute Force");
                }

                new Thread(() -> {
                    try {
                        Thread.sleep(LOCKOUT_TIME_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    loginAttempts.get(clientIp).set(0);
                }).start();
            }
        }

        chain.doFilter(request, response);
    }
}

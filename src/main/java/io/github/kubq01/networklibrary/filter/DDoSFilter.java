package io.github.kubq01.networklibrary.filter;

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
@Order(1)
public class DDoSFilter implements Filter {

    private static final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private static final int REQUEST_LIMIT = 100;
    private static final long TIME_WINDOW_MS = 60_000;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest) {
            String clientIp = request.getRemoteAddr();

            requestCounts.putIfAbsent(clientIp, new AtomicInteger(0));
            int currentCount = requestCounts.get(clientIp).incrementAndGet();

            log.info("[DDoS Protection] IP: {} - Liczba żądań: {}", clientIp, currentCount);

            if (currentCount > REQUEST_LIMIT) {
                log.warn("[DDoS Alert] Podejrzana aktywność z IP: {}", clientIp);
            }

            // Reset licznika po upływie okna czasowego
            new Thread(() -> {
                try {
                    Thread.sleep(TIME_WINDOW_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                requestCounts.get(clientIp).set(0);
            }).start();
        }

        chain.doFilter(request, response);
    }
}

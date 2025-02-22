package io.github.kubq01.networklibrary.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1) // Ustawienie kolejności wykonania filtra
public class SimpleLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SimpleLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("Żądanie otrzymane: {}", request.getRemoteAddr());
        chain.doFilter(request, response);
        log.info("Odpowiedź wysłana");
    }
}

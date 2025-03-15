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
import java.util.regex.Pattern;

@Slf4j
@Component
@Order(3)
public class SQLInjectionFilter implements Filter {

    private static final Pattern SQL_PATTERN = Pattern.compile(".*([';]+|(--)+).*", Pattern.CASE_INSENSITIVE);

    private final EmailAlertService emailAlertService;

    public SQLInjectionFilter(EmailAlertService emailAlertService) {
        this.emailAlertService = emailAlertService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest) {
            String query = httpRequest.getQueryString();

            if (query != null && SQL_PATTERN.matcher(query).matches()) {
                log.warn("[SQL Injection Alert] Podejrzane zapytanie: {}", query);
                emailAlertService.sendAlert("Podejrzany ruch z IP: " + request.getRemoteAddr() + ". Podejrzenie ataku SQL Injection");
            }
        }

        chain.doFilter(request, response);
    }
}

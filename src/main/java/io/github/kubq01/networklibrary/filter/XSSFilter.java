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
@Order(4)
public class XSSFilter implements Filter {

    private static final Pattern XSS_PATTERN = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);

    private final EmailAlertService emailAlertService;

    public XSSFilter(EmailAlertService emailAlertService) {
        this.emailAlertService = emailAlertService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest) {
            String query = httpRequest.getQueryString();

            if (query != null && XSS_PATTERN.matcher(query).matches()) {
                log.warn("[XSS Alert] Wykryto podejrzane zapytanie: {}", query);
                emailAlertService.sendAlert("Podejrzany ruch z IP: " + request.getRemoteAddr() + ". Podejrzenie ataku XXS");
            }
        }

        chain.doFilter(request, response);
    }
}
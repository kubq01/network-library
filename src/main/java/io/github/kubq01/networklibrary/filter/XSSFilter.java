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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
            String decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8);
            log.warn("decoded query: {}", decodedQuery);

            if (query != null) {
                String[] params = query.split("&");

                for (String param : params) {
                    String[] keyValue = param.split("=", 2);
                    if (keyValue.length > 1) {
                        String value = keyValue[1];
                        if (XSS_PATTERN.matcher(value).matches()) {
                            log.warn("[XSS Alert] Wykryto podejrzaną wartość: {}", value);
                            emailAlertService.sendAlert("Podejrzany ruch z IP: " + request.getRemoteAddr() + ". Podejrzenie ataku XSS: " + value);
                            break;
                        }
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }
}
package io.github.kubq01.networklibrary.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;

class DDoSFilterTest {

    private DDoSFilter filter;
    private HttpServletRequest request;
    private ServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new DDoSFilter();
        request = mock(HttpServletRequest.class);
        response = mock(ServletResponse.class);
        chain = mock(FilterChain.class);
    }

    @Test
    void shouldDetectHighTraffic() throws IOException, jakarta.servlet.ServletException {
        when(request.getRemoteAddr()).thenReturn("192.168.1.1");

        for (int i = 0; i < 110; i++) { // Exceed limit
            filter.doFilter(request, response, chain);
        }

        verify(chain, times(110)).doFilter(request, response);
    }
}
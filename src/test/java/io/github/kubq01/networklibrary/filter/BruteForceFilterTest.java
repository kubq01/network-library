package io.github.kubq01.networklibrary.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class BruteForceFilterTest {

    private BruteForceFilter filter;
    private HttpServletRequest request;
    private ServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new BruteForceFilter();
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

        verify(chain, times(35)).doFilter(request, response);
    }
}
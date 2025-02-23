package io.github.kubq01.networklibrary.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class XSSFilterTest {

    private XSSFilter filter;
    private HttpServletRequest request;
    private ServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new XSSFilter();
        request = mock(HttpServletRequest.class);
        response = mock(ServletResponse.class);
        chain = mock(FilterChain.class);
    }

    @Test
    void shouldDetectXSS() throws IOException, jakarta.servlet.ServletException {
        when(request.getQueryString()).thenReturn("<script>alert('XSS')</script>");

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }
}
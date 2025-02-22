package io.github.kubq01.networklibrary.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class SimpleLoggingFilterTest {

    private SimpleLoggingFilter simpleLoggingFilter;
    private ServletRequest request;
    private ServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        simpleLoggingFilter = new SimpleLoggingFilter();
        request = mock(ServletRequest.class);
        response = mock(ServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @Test
    void testDoFilter() throws IOException, jakarta.servlet.ServletException {
        simpleLoggingFilter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }
}
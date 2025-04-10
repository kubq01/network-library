package io.github.kubq01.networklibrary.filter;

import io.github.kubq01.networklibrary.jade.JADEHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

class UnifiedSecurityFilterTest {

    private UnifiedSecurityFilter filter;
    private HttpServletRequest request;
    private ServletResponse response;
    private FilterChain chain;

    private final String expectedContent = "192.168.1.10|/login|user=admin";

    @BeforeEach
    void setUp() {
        filter = new UnifiedSecurityFilter(); // bez Testable, bo nie tworzymy już AID
        request = mock(HttpServletRequest.class);
        response = mock(ServletResponse.class);
        chain = mock(FilterChain.class);
    }

    @Test
    void shouldSendMessageToEachAgentIndividually() throws Exception {
        when(request.getRemoteAddr()).thenReturn("192.168.1.10");
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getQueryString()).thenReturn("user=admin");

        try (MockedStatic<JADEHelper> jadeHelperMock = mockStatic(JADEHelper.class)) {

            filter.doFilter(request, response, chain);

            jadeHelperMock.verify(() -> JADEHelper.sendMessage("ddos-agent", expectedContent), times(1));
            jadeHelperMock.verify(() -> JADEHelper.sendMessage("brute-agent", expectedContent), times(1));
            jadeHelperMock.verify(() -> JADEHelper.sendMessage("sql-agent", expectedContent), times(1));

            verify(chain).doFilter(request, response);
        }
    }
}
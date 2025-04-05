package io.github.kubq01.networklibrary.filter;

import io.github.kubq01.networklibrary.jade.JADEHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Iterator;

import static org.mockito.Mockito.*;

class UnifiedSecurityFilterTest {

    private UnifiedSecurityFilter filter;
    private HttpServletRequest request;
    private ServletResponse response;
    private FilterChain chain;

    private final String expectedContent = "192.168.1.10|/login|user=admin";

    @BeforeEach
    void setUp() {
        filter = new TestableUnifiedSecurityFilter();
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

            jadeHelperMock.verify(() -> JADEHelper.sendMessage(argThat(msg ->
                    msg.getContent().equals(expectedContent) &&
                            hasReceiver(msg, "ddos-agent")
            )), times(1));

            jadeHelperMock.verify(() -> JADEHelper.sendMessage(argThat(msg ->
                    msg.getContent().equals(expectedContent) &&
                            hasReceiver(msg, "brute-agent")
            )), times(1));

            jadeHelperMock.verify(() -> JADEHelper.sendMessage(argThat(msg ->
                    msg.getContent().equals(expectedContent) &&
                            hasReceiver(msg, "sql-agent")
            )), times(1));

            verify(chain).doFilter(request, response);
        }
    }

    private static boolean hasReceiver(ACLMessage msg, String expectedName) {
        Iterator<AID> receivers = msg.getAllReceiver();
        while (receivers.hasNext()) {
            AID aid = receivers.next();
            if (aid.getName().equals(expectedName)) return true;
        }
        return false;
    }

    static class TestableUnifiedSecurityFilter extends UnifiedSecurityFilter {
        @Override
        protected AID createAgentId(String name) {
            AID mockAID = mock(AID.class);
            when(mockAID.getName()).thenReturn(name);
            return mockAID;
        }
    }
}
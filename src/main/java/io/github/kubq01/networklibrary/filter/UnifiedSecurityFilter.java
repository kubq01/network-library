package io.github.kubq01.networklibrary.filter;

import io.github.kubq01.networklibrary.jade.JADEHelper;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
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

@Slf4j
@Component
@Order(1)
public class UnifiedSecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest) {
            String clientIp = request.getRemoteAddr();
            String uri = httpRequest.getRequestURI();
            String query = httpRequest.getQueryString() != null ? httpRequest.getQueryString() : "";

            String fullInfo = clientIp + "|" + uri + "|" + query;

            sendToAgent("ddos-agent", fullInfo);
            sendToAgent("brute-agent", fullInfo);
            sendToAgent("sql-agent", fullInfo);
            sendToAgent("xss-agent", fullInfo);
        }

        chain.doFilter(request, response);
    }

    protected void sendToAgent(String agentName, String content) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(createAgentId(agentName));
        msg.setContent(content);
        JADEHelper.sendMessage(msg);
    }

    protected AID createAgentId(String name) {
        return new AID(name, AID.ISLOCALNAME);
    }
}

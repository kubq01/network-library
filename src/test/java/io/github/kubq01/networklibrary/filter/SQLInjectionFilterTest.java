package io.github.kubq01.networklibrary.filter;

import jade.lang.acl.ACLMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SQLInjectionFilterTest {

    private SQLInjectionFilter agent;
    private AtomicBoolean sent = new AtomicBoolean(false);
    private String lastMsg;

    @BeforeEach
    void setup() {
        agent = new SQLInjectionFilter() {
            @Override
            protected void sendAlert(String message) {
                sent.set(true);
                lastMsg = message;
            }
        };
    }

    @Test
    void shouldDetectSqlInjection() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent("127.0.0.1|/search|q=1%27%20OR%201%3D1");

        agent.newRequest(msg);

        assertTrue(sent.get());
        assertTrue(lastMsg.contains("SQL Injection"));
    }
}
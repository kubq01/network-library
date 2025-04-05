package io.github.kubq01.networklibrary.filter;

import jade.lang.acl.ACLMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BruteForceFilterTest {

    private BruteForceFilter agent;
    private AtomicBoolean sent = new AtomicBoolean(false);
    private String lastMessage;

    @BeforeEach
    void setup() {
        agent = new BruteForceFilter() {
            @Override
            protected void sendAlert(String message) {
                sent.set(true);
                lastMessage = message;
            }
        };
    }

    @Test
    void shouldDetectBruteForce() {
        String content = "192.168.0.1|/login|user=admin";

        for (int i = 0; i < 35; i++) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent(content);
            agent.newRequest(msg);
        }

        assertTrue(sent.get());
        assertNotNull(lastMessage);
        assertTrue(lastMessage.contains("Brute Force"));
    }
}
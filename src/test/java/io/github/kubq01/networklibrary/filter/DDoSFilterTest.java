package io.github.kubq01.networklibrary.filter;

import jade.lang.acl.ACLMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DDoSFilterTest  {

    private DDoSFilter agent;
    private AtomicBoolean sent = new AtomicBoolean(false);

    @BeforeEach
    void setup() {
        agent = new DDoSFilter() {
            @Override
            protected void sendAlert(String message) {
                sent.set(true);
            }
        };
    }

    @Test
    void shouldDetectDDoS() {
        for (int i = 0; i < 120; i++) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("10.0.0.5|/test|");
            agent.newRequest(msg);
        }

        assertTrue(sent.get());
    }
}
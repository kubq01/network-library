package io.github.kubq01.networklibrary.filter;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BruteForceFilter extends Agent {

    private static final Map<String, AtomicInteger> loginAttempts = new ConcurrentHashMap<>();
    private static final int ATTEMPT_LIMIT = 30;
    private static final long LOCKOUT_TIME_MS = 300_000;

    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) newRequest(msg);
                else block();
            }
        });
    }

    public void newRequest(ACLMessage msg) {
        String[] parts = msg.getContent().split("\\|", 3);
        if (parts.length < 2) return;

        String ip = parts[0];
        String uri = parts[1];

        if (uri.contains("/login")) {
            loginAttempts.putIfAbsent(ip, new AtomicInteger(0));
            int attempts = loginAttempts.get(ip).incrementAndGet();

            if (attempts > ATTEMPT_LIMIT) {
                sendAlert("Brute Force Alert from IP: " + ip);
            }

            new Thread(() -> {
                try { Thread.sleep(LOCKOUT_TIME_MS); } catch (InterruptedException e) {}
                loginAttempts.get(ip).set(0);
            }).start();
        }
    }

    protected void sendAlert(String message) {
        ACLMessage alert = new ACLMessage(ACLMessage.INFORM);
        alert.addReceiver(new AID("alert-agent", AID.ISLOCALNAME));
        alert.setContent(message);
        send(alert);
    }
}
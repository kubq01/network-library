package io.github.kubq01.networklibrary.filter;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class DDoSFilter extends Agent {

    private static final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private static final int REQUEST_LIMIT = 100;
    private static final long TIME_WINDOW_MS = 60_000;

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
        if (parts.length < 1) return;

        String ip = parts[0];
        requestCounts.putIfAbsent(ip, new AtomicInteger(0));
        int count = requestCounts.get(ip).incrementAndGet();

        if (count > REQUEST_LIMIT) {
            sendAlert("DDoS Alert from IP: " + ip);
        }

        new Thread(() -> {
            try { Thread.sleep(TIME_WINDOW_MS); } catch (InterruptedException e) {}
            requestCounts.get(ip).set(0);
        }).start();
    }

    protected void sendAlert(String message) {
        ACLMessage alert = new ACLMessage(ACLMessage.INFORM);
        alert.addReceiver(new AID("alert-agent", AID.ISLOCALNAME));
        alert.setContent(message);
        send(alert);
    }
}
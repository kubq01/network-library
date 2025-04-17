package io.github.kubq01.networklibrary.filter;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Slf4j
public class SQLInjectionFilter extends Agent {
    private static final Pattern SQL_PATTERN = Pattern.compile(".*([';]+|(--)+).*", Pattern.CASE_INSENSITIVE);

    @Override
    protected void setup() {
        log.info("SQLInjectionFilter setup");
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                log.info("Sql agent action");
                ACLMessage msg = receive();
                if (msg != null) newRequest(msg);
                else block();
            }
        });
    }

    public void newRequest(ACLMessage msg) {
        String[] parts = msg.getContent().split("\\|", 3);
        if (parts.length < 3) return;

        String ip = parts[0];
        String query = URLDecoder.decode(parts[2], StandardCharsets.UTF_8);

        for (String param : query.split("&")) {
            String[] kv = param.split("=", 2);
            if (kv.length > 1 && SQL_PATTERN.matcher(kv[1]).matches()) {
                sendAlert("SQL Injection from IP: " + ip + " value: " + kv[1]);
                break;
            }
        }
    }

    protected void sendAlert(String message) {
        ACLMessage alert = new ACLMessage(ACLMessage.INFORM);
        alert.addReceiver(new AID("alert-agent", AID.ISLOCALNAME));
        alert.setContent(message);
        send(alert);
    }
}
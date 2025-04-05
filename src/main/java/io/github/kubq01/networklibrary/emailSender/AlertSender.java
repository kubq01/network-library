package io.github.kubq01.networklibrary.emailSender;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Setter;

public class AlertSender extends Agent {

    @Setter
    private static EmailAlertService emailService;

    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && emailService != null) {
                    emailService.sendAlert(msg.getContent());
                } else block();
            }
        });
    }
}

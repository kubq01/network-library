package io.github.kubq01.networklibrary.jade;

import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import lombok.Setter;

import java.util.UUID;

public class JADEHelper {

    @Setter
    private static AgentContainer container;

    public static void sendMessage(ACLMessage msg) {
        try {
            AgentController sender = container.createNewAgent(
                    "sender-" + UUID.randomUUID(),
                    OneShotSenderAgent.class.getName(),
                    new Object[]{msg}
            );
            sender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


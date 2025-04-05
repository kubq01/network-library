package io.github.kubq01.networklibrary.jade;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class OneShotSenderAgent extends Agent {

    private final ACLMessage message;

    public OneShotSenderAgent(ACLMessage message) {
        this.message = message;
    }

    @Override
    protected void setup() {
        send(message);
        doDelete();
    }
}


package io.github.kubq01.networklibrary.jade;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OneShotSenderAgent extends Agent {

    @Override
    protected void setup() {
        log.info("one shot agent");
        Object[] args = getArguments();

        if (args == null || args.length < 2) {
            doDelete();
            return;
        }

        String agentName = (String) args[0];
        String content = (String) args[1];

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID(agentName, AID.ISLOCALNAME));
        msg.setContent(content);
        send(msg);

        log.info("one shot agent success");

        doDelete();
    }
}
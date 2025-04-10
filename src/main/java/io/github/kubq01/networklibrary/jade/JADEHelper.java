package io.github.kubq01.networklibrary.jade;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class JADEHelper {

    @Setter
    private static AgentContainer container;

    private static final Queue<Pair<String, String>> waitingMessages = new ConcurrentLinkedQueue<>();

    public static void sendMessage(String agentName, String content) {
        if (container == null) {
            log.warn("JADE container not ready yet, buffering message to agent: {}", agentName);
            waitingMessages.add(Pair.of(agentName, content));
            return;
        }

        sendNow(agentName, content);
    }

    public static void setContainer(AgentContainer c) {
        container = c;
        log.info("JADE container initialized, sending buffered messages...");

        while (!waitingMessages.isEmpty()) {
            Pair<String, String> msg = waitingMessages.poll();
            sendNow(msg.getLeft(), msg.getRight());
        }
    }

    private static void sendNow(String agentName, String content) {
        try {
            AgentController sender = container.createNewAgent(
                    "sender-" + UUID.randomUUID(),
                    OneShotSenderAgent.class.getName(),
                    new Object[]{agentName, content}
            );
            sender.start();
            log.info("send to one shot agent");
        } catch (Exception e) {
            log.error("Failed to create and start agent for message to: {}", agentName, e);
        }
    }
}

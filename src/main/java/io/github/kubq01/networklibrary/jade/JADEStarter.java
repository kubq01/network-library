package io.github.kubq01.networklibrary.jade;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.core.Runtime;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JADEStarter {

    @PostConstruct
    public void startJade() throws Exception {
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN, "true");
        p.setParameter(Profile.GUI, "false");

        AgentContainer container = Runtime.instance().createMainContainer(p);
        JADEHelper.setContainer(container); // <- podpinamy container po starcie JADE

        log.info("JADE Platform started and container set.");
    }
}

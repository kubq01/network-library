package io.github.kubq01.networklibrary.jade;

import io.github.kubq01.networklibrary.emailSender.AlertSender;
import io.github.kubq01.networklibrary.emailSender.EmailAlertService;
import io.github.kubq01.networklibrary.filter.BruteForceFilter;
import io.github.kubq01.networklibrary.filter.DDoSFilter;
import io.github.kubq01.networklibrary.filter.SQLInjectionFilter;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jade.core.Runtime;


@Component
public class JADEStarter {

    @Autowired EmailAlertService emailService;

    @PostConstruct
    public void start() throws Exception {
        Profile p = new ProfileImpl();
        AgentContainer container = Runtime.instance().createMainContainer(p);
        JADEHelper.setContainer(container);

        container.createNewAgent("ddos-agent", DDoSFilter.class.getName(), null).start();
        container.createNewAgent("brute-agent", BruteForceFilter.class.getName(), null).start();
        container.createNewAgent("sql-agent", SQLInjectionFilter.class.getName(), null).start();

        container.createNewAgent("alert-agent", AlertSender.class.getName(), null).start();
        AlertSender.setEmailService(emailService);
    }
}
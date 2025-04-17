package io.github.kubq01.networklibrary.config;

import io.github.kubq01.networklibrary.emailSender.AlertSender;
import io.github.kubq01.networklibrary.emailSender.EmailAlertService;
import io.github.kubq01.networklibrary.filter.BruteForceFilter;
import io.github.kubq01.networklibrary.filter.DDoSFilter;
import io.github.kubq01.networklibrary.filter.SQLInjectionFilter;
import io.github.kubq01.networklibrary.filter.UnifiedSecurityFilter;
import io.github.kubq01.networklibrary.jade.JADEHelper;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

import jade.core.Runtime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoConfiguration
public class NetworkLibraryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EmailAlertService emailAlertService(JavaMailSender mailSender) {
        return new EmailAlertService(mailSender);
    }

    @Bean
    @ConditionalOnMissingBean
    public UnifiedSecurityFilter unifiedSecurityFilter() {
        return new UnifiedSecurityFilter();
    }

    @Bean
    public AgentContainer agentContainer(EmailAlertService emailService) throws Exception {
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN, "true");
        p.setParameter(Profile.GUI, "false");

        AgentContainer container = Runtime.instance().createMainContainer(p);
        JADEHelper.setContainer(container);

        container.createNewAgent("ddos-agent", DDoSFilter.class.getName(), null).start();
        container.createNewAgent("brute-agent", BruteForceFilter.class.getName(), null).start();
        container.createNewAgent("sql-agent", SQLInjectionFilter.class.getName(), null).start();
        container.acceptNewAgent("alert-agent", new AlertSender(emailService)).start();

        log.info("JADE Platform started and container set.");

        return container;
    }

    @Bean
    public FilterRegistrationBean<UnifiedSecurityFilter> unifiedSecurityFilterRegistration(UnifiedSecurityFilter filter) {
        FilterRegistrationBean<UnifiedSecurityFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
package io.github.kubq01.networklibrary.config;

import io.github.kubq01.networklibrary.emailSender.EmailAlertService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@AutoConfiguration
public class NetworkLibraryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EmailAlertService emailAlertService(JavaMailSender mailSender) {
        return new EmailAlertService(mailSender);
    }
}
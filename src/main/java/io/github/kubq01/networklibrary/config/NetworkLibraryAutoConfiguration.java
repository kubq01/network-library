package io.github.kubq01.networklibrary.config;

import io.github.kubq01.networklibrary.filter.SimpleLoggingFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class NetworkLibraryAutoConfiguration {

    @Bean
    public FilterRegistrationBean<SimpleLoggingFilter> loggingFilter() {
        FilterRegistrationBean<SimpleLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SimpleLoggingFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
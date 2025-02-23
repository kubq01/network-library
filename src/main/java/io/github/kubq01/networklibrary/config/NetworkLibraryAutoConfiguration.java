package io.github.kubq01.networklibrary.config;

import io.github.kubq01.networklibrary.filter.BruteForceFilter;
import io.github.kubq01.networklibrary.filter.DDoSFilter;
import io.github.kubq01.networklibrary.filter.SQLInjectionFilter;
import io.github.kubq01.networklibrary.filter.XSSFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class NetworkLibraryAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "network.security.ddos-filter-enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<DDoSFilter> ddosFilterRegistrationBean() {
        return createFilter(new DDoSFilter(), "/*", 1);
    }

    @Bean
    @ConditionalOnProperty(name = "network.security.brute-force-filter-enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<BruteForceFilter> bruteForceFilterRegistrationBean() {
        return createFilter(new BruteForceFilter(), "/login", 2);
    }

    @Bean
    @ConditionalOnProperty(name = "network.security.sql-filter-enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<SQLInjectionFilter> sqlInjectionFilterregistrationBean() {
        return createFilter(new SQLInjectionFilter(), "/*", 3);
    }

    @Bean
    @ConditionalOnProperty(name = "network.security.xss-filter-enabled", havingValue = "true", matchIfMissing = true)
    public FilterRegistrationBean<XSSFilter> xssFilterRegistrationBean() {
        return createFilter(new XSSFilter(), "/*", 4);
    }

    private <T extends Filter> FilterRegistrationBean<T> createFilter(T filter, String url, int order) {
        FilterRegistrationBean<T> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(url);
        registrationBean.setOrder(order);
        return registrationBean;
    }
}
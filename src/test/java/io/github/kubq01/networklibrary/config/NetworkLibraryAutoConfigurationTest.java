package io.github.kubq01.networklibrary.config;

import io.github.kubq01.networklibrary.filter.BruteForceFilter;
import io.github.kubq01.networklibrary.filter.DDoSFilter;
import io.github.kubq01.networklibrary.filter.SQLInjectionFilter;
import io.github.kubq01.networklibrary.filter.XSSFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NetworkLibraryAutoConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testFilterIsRegistered() {
        assertThat(applicationContext.getBean(DDoSFilter.class)).isNotNull();
        assertThat(applicationContext.getBean(BruteForceFilter.class)).isNotNull();
        assertThat(applicationContext.getBean(SQLInjectionFilter.class)).isNotNull();
        assertThat(applicationContext.getBean(XSSFilter.class)).isNotNull();
    }
}
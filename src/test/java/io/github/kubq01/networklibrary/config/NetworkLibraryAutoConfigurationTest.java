package io.github.kubq01.networklibrary.config;

import io.github.kubq01.networklibrary.filter.SimpleLoggingFilter;
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
        assertThat(applicationContext.getBean(SimpleLoggingFilter.class)).isNotNull();
    }
}
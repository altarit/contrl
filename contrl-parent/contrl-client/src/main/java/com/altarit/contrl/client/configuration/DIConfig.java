package com.altarit.contrl.client.configuration;

import com.altarit.contrl.client.workers.WorkPool;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.altarit.contrl")
public class DIConfig {

    @Bean
    public WorkPool workPool() {
        return new WorkPool(1);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}

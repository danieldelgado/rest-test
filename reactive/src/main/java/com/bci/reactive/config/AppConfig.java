package com.bci.reactive.config;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class AppConfig {

    public void configureUTC() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
    }

    public AppConfig() {
        configureUTC();
    }

}
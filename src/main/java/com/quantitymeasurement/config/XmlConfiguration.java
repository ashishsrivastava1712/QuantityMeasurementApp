package com.quantitymeasurement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.xml.JacksonXmlHttpMessageConverter;

@Configuration
public class XmlConfiguration {

    @Bean
    public JacksonXmlHttpMessageConverter jacksonXmlHttpMessageConverter() {
        return new JacksonXmlHttpMessageConverter();
    }
}
package com.shop.online.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

import java.text.SimpleDateFormat;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Configuration
@EnableAspectJAutoProxy
public class ApplicationConfig {

    public static final String DATE_TIME_FORMAT = "dd-MM-yyyy hh:mm:ss";

    @Bean
    @Profile("!prod")
    public ObjectMapper objectMapperNotProd() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_TIME_FORMAT));
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    @Profile("prod")
    public ObjectMapper objectMapperProd() {
        return new ObjectMapper();
    }

}

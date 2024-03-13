package com.shop.online.demo.config;

import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationConfig {

    public static final String DATE_TIME_FORMAT = "dd-MM-yyyy hh:mm:ss";

//    @Bean
//    @Profile("!prod")
//    public ObjectMapper objectMapperNotProd() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setDateFormat(new SimpleDateFormat(DATE_TIME_FORMAT));
//        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
//        return objectMapper;
//    }
//
//    @Bean
//    @Profile("prod")
//    public ObjectMapper objectMapperProd() {
//        return new ObjectMapper();
//    }

}

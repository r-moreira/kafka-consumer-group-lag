package com.rodrigo.kafkaconsumerlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
public class KafkaConsumerLagApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerLagApplication.class);

    @Value("UTC")
    private String timezone;

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
        LOGGER.info("Spring boot application running in {} timezone : {}", timezone, new Date());
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumerLagApplication.class, args);
    }

}

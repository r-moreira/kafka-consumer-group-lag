package com.rodrigo.kafkaconsumerlag.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("#{${kafka.topic.consumer.group}}")
    public Map<String,String> topicAndConsumerGroup;
}

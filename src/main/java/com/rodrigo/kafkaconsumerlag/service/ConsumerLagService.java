package com.rodrigo.kafkaconsumerlag.service;

import com.rodrigo.kafkaconsumerlag.config.KafkaConsumerConfig;
import com.rodrigo.kafkaconsumerlag.model.ConsumerGroupLag;
import com.rodrigo.kafkaconsumerlag.repository.ConsumerGroupLagRepository;
import com.rodrigo.kafkaconsumerlag.service.utils.KafkaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ConsumerLagService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerLagService.class);

    @Autowired
    KafkaConsumerConfig kafkaConsumerConfig;

    @Autowired
    ConsumerGroupLagRepository consumerGroupLagRepository;

    public void execute() {

        for (Map.Entry<String,String> entry : kafkaConsumerConfig.getTopicAndConsumerGroup().entrySet()) {
            List<ConsumerGroupLag> consumerGroupLagList = KafkaUtils.getConsumerGroupMetadataFromTopic(
                    kafkaConsumerConfig.getBootstrapAddress(),
                    entry.getValue(),
                    entry.getKey());

            consumerGroupLagRepository.saveAll(consumerGroupLagList);
            LOGGER.info(consumerGroupLagList.toString());
        }

    }
}

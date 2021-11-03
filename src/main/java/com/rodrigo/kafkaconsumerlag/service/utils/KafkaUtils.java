package com.rodrigo.kafkaconsumerlag.service.utils;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import com.rodrigo.kafkaconsumerlag.model.ConsumerGroupLag;
import com.rodrigo.kafkaconsumerlag.model.PartitionMetadata;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;

@Component
public class KafkaUtils {

    public static List<ConsumerGroupLag> getConsumerGroupMetadataFromTopic(String bootstrapAddress, String groupId, String topic) {

        Map<TopicPartition, Long> partitionsEndOffset = getPartitionsEndOffset(bootstrapAddress, groupId, topic);

        Set<TopicPartition> topicPartitions = new HashSet<>();
        for (Entry<TopicPartition, Long> s : partitionsEndOffset.entrySet()) {
            topicPartitions.add(s.getKey());
        }

        KafkaConsumer<String, Object> consumer = createNewConsumer(bootstrapAddress, groupId);
        Map<TopicPartition, OffsetAndMetadata> committedOffsetMeta = consumer.committed(topicPartitions);

        BinaryOperator<PartitionMetadata> mergeFunction = (a, b) -> {
            throw new IllegalStateException();
        };

        Map<TopicPartition, PartitionMetadata> partitionMetadata = partitionsEndOffset.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, entry -> {

                    long currentOffset = 0;
                    OffsetAndMetadata committed = committedOffsetMeta.get(entry.getKey());

                    if(committed != null) {
                        currentOffset = committed.offset();
                    }

                    return new PartitionMetadata(entry.getKey().partition(), entry.getValue(), currentOffset);
                }, mergeFunction));

        List<PartitionMetadata> partitionMetadataList = new ArrayList<>();

        for (Map.Entry<TopicPartition, PartitionMetadata> entry : partitionMetadata.entrySet()) {
            partitionMetadataList.add(entry.getValue());
        }

        long topicLag = partitionMetadataList.stream().mapToLong(PartitionMetadata::getConsumerLag).sum();
        List<ConsumerGroupLag> consumerGroupLagList = new ArrayList<>();

        for (PartitionMetadata data : partitionMetadataList) {
            consumerGroupLagList.add(new ConsumerGroupLag(
                    topic,
                    groupId,
                    data.getPartition(),
                    data.getCurrentOffset(),
                    data.getEndOffset(),
                    data.getConsumerLag(),
                    topicLag));
        }

        return consumerGroupLagList;
    }

     private static Map<TopicPartition, Long> getPartitionsEndOffset(String bootsStrapAddress, String groupId, String topic) {
        Map<TopicPartition, Long> endOffsets = new ConcurrentHashMap<>();

        KafkaConsumer<?, ?> consumer = createNewConsumer(bootsStrapAddress, groupId);
        List<PartitionInfo> partitionInfoList = consumer.partitionsFor(topic);

        List<TopicPartition> topicPartitions = partitionInfoList.stream()
                .map(pi -> new TopicPartition(topic, pi.partition()))
                .collect(Collectors.toList());

        consumer.assign(topicPartitions);
        consumer.seekToEnd(topicPartitions);
       
        topicPartitions.forEach(topicPartition -> endOffsets.put(topicPartition, consumer.position(topicPartition)));

        consumer.close();
        return endOffsets;
    }

     private static KafkaConsumer<String, Object> createNewConsumer(String bootstrapAddress, String groupId) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new KafkaConsumer<>(properties);
    }
}
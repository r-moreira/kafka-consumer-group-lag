package com.rodrigo.kafkaconsumerlag.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

@Document(indexName = "control-consumer-group-lag")
public class ConsumerGroupLag {

    @Id
    @JsonProperty("id")
    private String id;

    @Field(type = FieldType.Date, name = "@timestamp")
    private final Instant instant;

    @Field(type = FieldType.Keyword, name = "topic")
    private final String topic;

    @Field(type = FieldType.Keyword, name = "consumer_group")
    private final String consumerGroup;

    @Field(type = FieldType.Long, name = "topic_total_consumer_lag")
    private final Long topicTotalConsumerLag;

    @Field(type = FieldType.Keyword, name = "partition")
    private final int partition;

    @Field(type = FieldType.Long, name = "partition_consumer_lag")
    private final long partitionConsumerLag;

    @Field(type = FieldType.Long, name = "end_offset")
    private final long endOffset;

    @Field(type = FieldType.Long, name = "current_offset")
    private final long currentOffset;

    public ConsumerGroupLag(String topic,
                            String consumerGroup,
                            int partition,
                            long currentOffset,
                            long endOffset,
                            long consumerLag,
                            long topicTotalConsumerLag) {
        this.instant = Instant.now();
        this.topic = topic;
        this.consumerGroup = consumerGroup;
        this.partition = partition;
        this.endOffset = endOffset;
        this.currentOffset = currentOffset;
        this.partitionConsumerLag = consumerLag;
        this.topicTotalConsumerLag = topicTotalConsumerLag;
    }

    @Override
    public String toString() {
        return "ConsumerGroupLag{" +
                "id='" + id + '\'' +
                ", instant=" + instant +
                ", topic='" + topic + '\'' +
                ", consumerGroup='" + consumerGroup + '\'' +
                ", topicTotalConsumerLag=" + topicTotalConsumerLag +
                ", partition=" + partition +
                ", partitionConsumerLag=" + partitionConsumerLag +
                ", endOffset=" + endOffset +
                ", currentOffset=" + currentOffset +
                '}';
    }
}

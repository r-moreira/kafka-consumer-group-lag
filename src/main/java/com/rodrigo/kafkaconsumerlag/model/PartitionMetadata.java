package com.rodrigo.kafkaconsumerlag.model;

import lombok.Getter;

@Getter
public class PartitionMetadata {

    private final int partition;
    private final long consumerLag;
    private final long endOffset;
    private final long currentOffset;

    public PartitionMetadata(int partition, long partitionEndOffset, long partitionCurrentOffset) {
        this.partition = partition;
        this.endOffset = partitionEndOffset;
        this.currentOffset = partitionCurrentOffset;
        this.consumerLag = partitionEndOffset - partitionCurrentOffset;
    }

    @Override
    public String toString() {
        return "PartitionMetadata{" +
                "partition=" + partition +
                ", consumerLag=" + consumerLag +
                ", endOffset=" + endOffset +
                ", currentOffset=" + currentOffset +
                '}';
    }
}

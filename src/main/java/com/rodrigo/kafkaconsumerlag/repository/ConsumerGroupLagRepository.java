package com.rodrigo.kafkaconsumerlag.repository;

import com.rodrigo.kafkaconsumerlag.model.ConsumerGroupLag;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerGroupLagRepository extends ElasticsearchRepository<ConsumerGroupLag, String> {
}

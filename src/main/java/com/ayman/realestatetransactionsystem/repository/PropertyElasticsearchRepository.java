package com.ayman.realestatetransactionsystem.repository;

import com.ayman.realestatetransactionsystem.model.PropertyDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyElasticsearchRepository extends ElasticsearchRepository<Long, PropertyDocument> {
}

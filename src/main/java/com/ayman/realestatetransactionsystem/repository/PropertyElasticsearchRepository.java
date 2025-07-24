package com.ayman.realestatetransactionsystem.repository;

import com.ayman.realestatetransactionsystem.model.PropertyDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyElasticsearchRepository extends ElasticsearchRepository<PropertyDocument, Long> {
    List<PropertyDocument> findByCity(String city);

    List<PropertyDocument> findByPriceBetween(double priceAfter, double priceBefore);
}

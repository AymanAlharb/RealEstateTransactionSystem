package com.ayman.realestatetransactionsystem.repository;

import com.ayman.realestatetransactionsystem.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    City getCityByNameAndRegion(String cityName, String regionName);
}

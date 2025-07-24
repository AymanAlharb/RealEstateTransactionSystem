package com.ayman.realestatetransactionsystem.repository;

import com.ayman.realestatetransactionsystem.model.Property;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Property p where p.id= :id")
    Property findPropertyById(Long id);
}

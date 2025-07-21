package com.ayman.realestatetransactionsystem.repository;

import com.ayman.realestatetransactionsystem.model.PropertyOwnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyOwnerShipRepository extends JpaRepository<Long, PropertyOwnership> {
}

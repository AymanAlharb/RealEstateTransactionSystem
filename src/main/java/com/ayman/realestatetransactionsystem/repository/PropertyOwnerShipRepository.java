package com.ayman.realestatetransactionsystem.repository;

import com.ayman.realestatetransactionsystem.model.entity.Property;
import com.ayman.realestatetransactionsystem.model.entity.PropertyOwnership;
import com.ayman.realestatetransactionsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyOwnerShipRepository extends JpaRepository<PropertyOwnership, Long> {
    PropertyOwnership getPropertyOwnershipByPropertyAndOwner(Property property, User owner);
}

package com.ayman.realestatetransactionsystem.repository;

import com.ayman.realestatetransactionsystem.model.Property;
import com.ayman.realestatetransactionsystem.model.PropertyOwnership;
import com.ayman.realestatetransactionsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyOwnerShipRepository extends JpaRepository<PropertyOwnership, Long> {
    PropertyOwnership getPropertyOwnershipByPropertyAndOwner(Property property, User owner);
}

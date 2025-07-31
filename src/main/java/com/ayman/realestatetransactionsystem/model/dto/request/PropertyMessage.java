package com.ayman.realestatetransactionsystem.model.dto.request;

import com.ayman.realestatetransactionsystem.model.entity.City;
import com.ayman.realestatetransactionsystem.model.entity.PropertyOwnership;
import com.ayman.realestatetransactionsystem.model.entity.Transaction;
import com.ayman.realestatetransactionsystem.model.entity.User;
import com.ayman.realestatetransactionsystem.model.enums.PropertyStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class PropertyMessage {

    private Long id;

    private String title;

    private String description;

    private double price;

    private String status;

    private String location;

    private String city;

    private String region;

    private String ownerName;
}

package com.ayman.realestatetransactionsystem.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


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

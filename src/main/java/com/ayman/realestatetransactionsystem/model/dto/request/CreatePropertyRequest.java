package com.ayman.realestatetransactionsystem.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePropertyRequest {

    @NotEmpty(message = "The title can not be empty")
    @Size(min = 3,
            max = 20,
            message = "The title can not be longer than 20 or smaller than 3")
    private String title;

    @NotEmpty(message = "The description can not be empty")
    @Size(min = 5,
            max = 256,
            message = "The description can not be longer than 256 or smaller than 5")
    private String description;

    @NotNull(message = "The price can not be null")
    @Positive(message = "The price must be a positive number")
    private double price;

    @NotEmpty(message = "The status can not be empty")
    @Pattern(regexp = "^(?i)(AVAILABLE|LOCKED|SOLD|HIDDEN)$")
    private String status;

    @NotEmpty(message = "The location can not be empty")
    private String location;

    @NotEmpty(message = "The city name can not be empty")
    private String cityName;

    @NotEmpty(message = "The region name can not be empty")
    private String regionName;

    @NotEmpty(message = "The owner username can not be empty")
    private String ownerUsername;

}

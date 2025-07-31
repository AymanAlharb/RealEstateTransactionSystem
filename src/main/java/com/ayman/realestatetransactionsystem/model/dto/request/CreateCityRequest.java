package com.ayman.realestatetransactionsystem.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCityRequest {
    @NotEmpty(message = "The city name can not be empty")
    private String name;

    @NotEmpty(message = "The region can not be empty")
    private String region;
}

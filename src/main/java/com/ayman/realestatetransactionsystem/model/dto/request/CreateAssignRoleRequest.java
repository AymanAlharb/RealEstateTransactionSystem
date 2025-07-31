package com.ayman.realestatetransactionsystem.model.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAssignRoleRequest {
    private String id;
    private String name;
}

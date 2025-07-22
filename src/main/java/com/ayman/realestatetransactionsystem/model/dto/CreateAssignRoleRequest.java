package com.ayman.realestatetransactionsystem.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAssignRoleRequest {
    private String id;
    private String name;
}

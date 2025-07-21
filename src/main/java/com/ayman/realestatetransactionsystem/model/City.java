package com.ayman.realestatetransactionsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(32) not null")
    private String name;

    @Column(columnDefinition = "varchar(32) not null")
    private String region;

    @OneToMany
    private Set<Property> properties;
}

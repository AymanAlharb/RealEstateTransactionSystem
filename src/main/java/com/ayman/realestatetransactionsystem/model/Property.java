package com.ayman.realestatetransactionsystem.model;

import com.ayman.realestatetransactionsystem.model.enums.PropertyStatusEnum;
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
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(20) not null")
    private String title;

    @Column(columnDefinition = "varchar(256) not null")
    private String description;

    @Column(columnDefinition = "double not null")
    private double price;

    @Column(columnDefinition = "varchar(10) not null")
    private PropertyStatusEnum status;

    @Column(columnDefinition = "varchar(256) not null")
    private String location;

    @ManyToOne
    private City city;

    @OneToMany
    private Set<PropertyOwnership> ownershipSet;

    @OneToMany
    private Set<Transaction> transactionSet;
}

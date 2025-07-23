package com.ayman.realestatetransactionsystem.model;

import com.ayman.realestatetransactionsystem.model.enums.PropertyStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @JsonIgnore
    private City city;

    @JsonIgnore
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private Set<PropertyOwnership> ownershipSet;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Transaction> transactionSet;
}

package com.ayman.realestatetransactionsystem.model;

import com.ayman.realestatetransactionsystem.model.enums.UserRoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(columnDefinition = "varchar(20) not null unique")
    private String username;

    @Column(columnDefinition = "varchar(256) not null")
    private String password;

    @Column(columnDefinition = "varchar(320) not null unique")
    private String email;

    @Column(columnDefinition = "varchar(16) not null unique")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(8) not null")
    private UserRoleEnum role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private BankAccount bankAccount;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Notification> notificationSet;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<PropertyOwnership> propertyOwnershipSet;

    @OneToMany(mappedBy = "seller")
    @JsonIgnore
    private Set<Transaction> sellerTransactionSet;

    @OneToMany(mappedBy = "buyer")
    @JsonIgnore
    private Set<Transaction> buyerTransactionSet;

    @OneToMany(mappedBy = "broker")
    @JsonIgnore
    private Set<Transaction> brokerTransactionSet;
}

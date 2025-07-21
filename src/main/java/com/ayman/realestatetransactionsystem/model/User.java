package com.ayman.realestatetransactionsystem.model;

import com.ayman.realestatetransactionsystem.model.enums.UserRoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
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

    @Column(columnDefinition = "varchar(8) not null")
    private UserRoleEnum role;

    @OneToOne
    private BankAccount bankAccount;

    @OneToMany
    private Set<Notification> notificationSet;

    @OneToMany
    private Set<PropertyOwnership> propertyOwnershipSet;

    @OneToMany
    private Set<Transaction> transactionSet;
}

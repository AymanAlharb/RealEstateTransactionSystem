package com.ayman.realestatetransactionsystem.model.entity;

import com.ayman.realestatetransactionsystem.model.enums.TransactionStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(24) not null")
    private TransactionStatusEnum status;

    @Column(columnDefinition = "varchar(256)")
    private String reasonOfFailure;

    @Column(columnDefinition = "date not null")
    private LocalDateTime date;

    @ManyToOne
    @JsonIgnore
    private Property property;

    @ManyToOne
    @JsonIgnore
    private User buyer;

    @ManyToOne
    @JsonIgnore
    private User seller;

    @ManyToOne
    @JsonIgnore
    private User broker;
}

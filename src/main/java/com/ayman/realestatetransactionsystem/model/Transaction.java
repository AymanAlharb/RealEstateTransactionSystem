package com.ayman.realestatetransactionsystem.model;

import com.ayman.realestatetransactionsystem.model.enums.TransectionStatusEnum;
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

    // Initially the transection is pending,
    // until both seller and broker approve the transection.
    private TransectionStatusEnum status = TransectionStatusEnum.PENDING;

    @Column(columnDefinition = "varchar(256) not null")
    private String reasonOfFailer;

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

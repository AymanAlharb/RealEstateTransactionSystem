package com.ayman.realestatetransactionsystem.model;

import com.ayman.realestatetransactionsystem.model.enums.TransectionStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
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
    private Property property;

    @ManyToOne
    private User buyer;

    @ManyToOne
    private User seller;

    @ManyToOne
    private User broker;
}

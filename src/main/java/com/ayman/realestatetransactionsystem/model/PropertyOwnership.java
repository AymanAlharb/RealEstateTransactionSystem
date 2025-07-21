package com.ayman.realestatetransactionsystem.model;

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
public class PropertyOwnership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "bool not null")
    private Boolean ownerFlag;

    @Column(columnDefinition = "date not null")
    private LocalDateTime ownershipDate;

    @ManyToOne
    private Property property;

    @ManyToOne
    private User user;
}

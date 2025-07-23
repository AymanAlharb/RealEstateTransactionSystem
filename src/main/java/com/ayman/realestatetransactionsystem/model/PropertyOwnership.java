package com.ayman.realestatetransactionsystem.model;

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
public class PropertyOwnership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "bool not null")
    private Boolean ownerFlag;

    @Column(columnDefinition = "date not null")
    private LocalDateTime ownershipDate;

    @ManyToOne
    @JsonIgnore
    private Property property;

    @ManyToOne
    @JsonIgnore
    private User owner;
}

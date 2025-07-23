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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(256) not null")
    private String message;

    @Column(columnDefinition = "date not null")
    private LocalDateTime date;

    @ManyToOne
    @JsonIgnore
    private User receiver;
}

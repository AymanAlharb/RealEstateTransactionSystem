package com.ayman.realestatetransactionsystem.model.dto;

import com.ayman.realestatetransactionsystem.model.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailStruct {
    private User receiver;
    private String body;
    private String subject;
}

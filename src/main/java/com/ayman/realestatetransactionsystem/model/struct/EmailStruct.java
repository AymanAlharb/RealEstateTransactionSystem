package com.ayman.realestatetransactionsystem.model.struct;

import com.ayman.realestatetransactionsystem.model.entity.User;
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

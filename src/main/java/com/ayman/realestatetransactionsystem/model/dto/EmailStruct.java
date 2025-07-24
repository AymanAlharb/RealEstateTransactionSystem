package com.ayman.realestatetransactionsystem.model.dto;

import com.ayman.realestatetransactionsystem.model.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailStruct {
    private User seller;
    private User broker;
    private User buyer;
}

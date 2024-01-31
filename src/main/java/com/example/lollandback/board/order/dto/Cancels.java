package com.example.lollandback.board.order.dto;

import lombok.Data;

@Data
public class Cancels {
    private String cancelReason;
    private String canceledAt;
    private Integer cancelAmount;
}

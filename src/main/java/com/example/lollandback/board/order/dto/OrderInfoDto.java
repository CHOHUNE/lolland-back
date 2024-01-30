package com.example.lollandback.board.order.dto;

import com.example.lollandback.board.order.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderInfoDto {
    private Long id;
    private String main_img_uri;
    private String order_name;
    private Double total_price;
    private OrderStatus order_status;
    private LocalDateTime order_reg_time;
}

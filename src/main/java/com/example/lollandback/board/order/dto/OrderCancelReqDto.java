package com.example.lollandback.board.order.dto;

import com.example.lollandback.board.order.domain.OrderStatus;

import com.example.lollandback.member.dto.MemberDto;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class OrderCancelReqDto {
    private Long id;
    private String main_img_uri;
    private String order_name;
    private String order_nano_id;
    private Double total_price;
    private OrderStatus order_status;
    private LocalDateTime order_reg_time;

    private MemberDto membersDto;
}

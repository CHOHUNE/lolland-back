package com.example.lollandback.board.order.dto;

import com.example.lollandback.board.order.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderInfoDetailDto {
    public Long id;
    public Long member_id;
    public String order_nano_id;
    public String order_name;
    public String receiver;
    public String email;
    public String phone;
    public String address;
    public String postalCode;
    public String requirement;
    public OrderStatus order_status;
    public Double total_price;
    public LocalDateTime order_reg_time;
    public List<OrderedProductDto> productList;
}

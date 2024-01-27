package com.example.lollandback.board.order.service;

import com.example.lollandback.board.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;

}

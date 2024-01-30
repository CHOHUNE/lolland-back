package com.example.lollandback.board.order.controller;

import com.example.lollandback.board.order.dto.*;
import com.example.lollandback.board.order.exception.CustomLogicException;
import com.example.lollandback.board.order.service.OrderService;
import com.example.lollandback.member.domain.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/my")
    public ResponseEntity fetchMyOrderInfo(@SessionAttribute("login") Member login,
                                           @RequestParam(value="p", defaultValue = "1")Integer page) {
        Long member_id = login.getId();
        try{
            Map<String, Object> map = orderService.fetchMyOrderInfo(member_id, page);
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/order-info")
    public ResponseEntity fetchOrderInfoDetail(@SessionAttribute("login") Member login, @RequestParam Long orderId) {
        try{
            OrderInfoDetailDto orderInfo = orderService.fetchOrderInfoDetail(orderId);
            if(orderInfo.member_id != login.getId()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.ok(orderInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/cancel-wait")
    public ResponseEntity cancelOrderRequest(@SessionAttribute("login") Member login,
                                             @RequestBody CancelRequestDto dto) {
        try {
            orderService.cancelWaitOrderStatus(dto.getOrderId());
            return ResponseEntity.ok().build();
        } catch (CustomLogicException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/toss")
    public ResponseEntity createOrderInfo(@SessionAttribute("login") Member login, @RequestBody OrderRequestDto dto) {
        if(login.getMember_login_id().equals(dto.getMember_login_id())) {
            try {
                OrderResDto res = orderService.createOrderInfo(login.getId(), dto);
                if(res != null) {
                    return ResponseEntity.ok(res);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } catch (CustomLogicException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", e.getMessage()));
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/toss/success")
    public ResponseEntity tossPaymentSuccess(@RequestBody PaymentRequestDto dto) throws JsonProcessingException {
        String paymentKey = dto.getPaymentKey();
        String orderId = dto.getOrderId();
        Long amount = dto.getAmount();
        try {
            PaymentSuccessDto response = orderService.tossPaymentSuccess(paymentKey, orderId, amount);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "code", HttpStatus.INTERNAL_SERVER_ERROR));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "code", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/toss/cancel")
    public ResponseEntity tossPaymentCancel(@RequestBody CancelRequestDto dto) {
        try {
            orderService.cancelOrderStatus(dto.getOrderId());
            return ResponseEntity.ok().build();
        } catch (CustomLogicException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}

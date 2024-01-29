package com.example.lollandback.board.order.controller;

import com.example.lollandback.board.order.dto.OrderRequestDto;
import com.example.lollandback.board.order.dto.OrderResDto;
import com.example.lollandback.board.order.dto.PaymentRequestDto;
import com.example.lollandback.board.order.dto.PaymentSuccessDto;
import com.example.lollandback.board.order.exception.CustomLogicException;
import com.example.lollandback.board.order.service.OrderService;
import com.example.lollandback.member.domain.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/toss")
    public ResponseEntity createOrderInfo(@SessionAttribute("login") Member login, @RequestBody OrderRequestDto dto) {
        System.out.println("OrderController.createOrderInfo");
        System.out.println("dto = " + dto);
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
            System.out.println("response = " + response);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/toss/cancel")
    public ResponseEntity tossPaymentCancel(@RequestBody String orderId) {
        try {
            orderService.cancelOrderStatus(orderId);
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

package com.example.lollandback.board.order.service;

import com.example.lollandback.board.order.domain.Order;
import com.example.lollandback.board.order.domain.OrderProductDetails;
import com.example.lollandback.board.order.domain.OrderStatus;
import com.example.lollandback.board.order.dto.OrderRequestDto;
import com.example.lollandback.board.order.dto.OrderResDto;
import com.example.lollandback.board.order.dto.PaymentSuccessDto;
import com.example.lollandback.board.order.dto.UpdateStockDto;
import com.example.lollandback.board.order.exception.CustomLogicException;
import com.example.lollandback.board.order.exception.ExceptionCode;
import com.example.lollandback.board.order.mapper.OrderMapper;
import com.example.lollandback.board.product.dto.ProductAndOptionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;

    @Value("${toss.pay.secretKey}")
    private String testSecretKey;

    public OrderResDto createOrderInfo(Long member_id, OrderRequestDto dto) {
        System.out.println("OrderService.createOrderInfo");
        // 결제 요청 시 값들 논리 검증
        // 해당 제품이 구매 가능한 제품들인지 (delete 상태인지 아닌지 확인)
        List<ProductAndOptionDto> productAndOptionDto = dto.getProductAndOptionDto();
        Double totalPrice = dto.getTotalPrice();
        Double checkTotalPrice = 3000.0; //기본 배송비
        for (ProductAndOptionDto optionDto : productAndOptionDto) {
            Long product_id = optionDto.getProduct_id();
            if(!orderMapper.isStatusNone(product_id)) {
                throw new CustomLogicException(ExceptionCode.NOT_FOR_SALE);
            }
            // 재고를 초과하지 않는지
            Long option_id = optionDto.getOption_id();
            Integer quantityOrdered = optionDto.getQuantity();
            if(orderMapper.checkStock(option_id, quantityOrdered)) {
                throw new CustomLogicException(ExceptionCode.OUT_OF_STOCK);
            }
            Double price = orderMapper.getPrice(product_id);
            checkTotalPrice += (price * quantityOrdered);
        }

        // 총 주문금액이 맞는지
        if(checkTotalPrice.intValue() != totalPrice.intValue()) {
            System.out.println("checkTotalPrice = " + checkTotalPrice);
            System.out.println("totalPrice = " + totalPrice);
            throw new CustomLogicException(ExceptionCode.PAYMENT_MISMATCH);
        }

        // 맞으면 order / orderProductDetails 나누어서 저장하기
        // order
        Order order = new Order(member_id, dto);
        System.out.println("order = " + order);
        orderMapper.saveOrder(order);
        // order 생성 후 orderProductDetails
        Long id = orderMapper.getOrderIdByNanoId(dto.getOrderId());
        System.out.println("id = " + id);
        if(id != null) {
            System.out.println("id != null");
            for (ProductAndOptionDto optionDto : productAndOptionDto) {
                Double price = orderMapper.getPrice(optionDto.getProduct_id());
                System.out.println("price = " + price);
                Double total_price = price * optionDto.getQuantity();
                System.out.println("total_price = " + total_price);
                OrderProductDetails productDetails = new OrderProductDetails(id, total_price, optionDto);
                System.out.println("productDetails = " + productDetails);
                orderMapper.saveProductDetails(productDetails);
            }

            // 저장 후 결제에 필요한 데이터 생성
            OrderResDto orderResDto = new OrderResDto(id, dto);
            System.out.println("orderResDto = " + orderResDto);
            return orderResDto;
        }

        return null;
    }

    public PaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) throws JsonProcessingException {
        System.out.println("OrderService.tossPaymentSuccess");
        Order order = verifyPayment(orderId, amount);
        PaymentSuccessDto response = requestPayment(paymentKey, orderId, amount);
        order.setOrder_status(OrderStatus.ORDERED);
        orderMapper.updateOrderStatus(order); // 주문 상태 변경

        // 옵션별로 재고 빼기
        List<UpdateStockDto> updateDto = orderMapper.findAllProductOptionByOrderId(order.getId());
        System.out.println("updateDto = " + updateDto);
        for(UpdateStockDto dto : updateDto) {
            System.out.println("dto = " + dto);
            Long quantity = dto.getQuantity().longValue();
            System.out.println("quantity = " + quantity);
            int rows = orderMapper.subtractOptionStock(dto.getOption_id(), quantity);
            System.out.println("rows = " + rows);
            // 총 재고 빼기
            orderMapper.subtractTotalStock(dto.getProduct_id(), dto.getQuantity());
        }
        return response;
    }

    private PaymentSuccessDto requestPayment(String paymentKey, String orderId, Long amount) {
            RestTemplate restTemplate = new RestTemplate();
            // 헤더 생성
            HttpHeaders headers = new HttpHeaders();
            String encodedAuthKey = new String(Base64.getEncoder().encode((testSecretKey + ":")
                    .getBytes(StandardCharsets.UTF_8)));
            headers.setBasicAuth(encodedAuthKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            //Param 생성
            Map<String, Object> params = Map.of("orderId", orderId, "amount", amount, "paymentKey", paymentKey);

            //Response 받을 Dto 생성
            PaymentSuccessDto response = null;
            // 요청 전송
            response = restTemplate.postForObject("https://api.tosspayments.com/v1/payments/confirm",
                    new HttpEntity<>(params, headers), PaymentSuccessDto.class);
            return response;
    }

    private Order verifyPayment(String orderId, Long amount) {
        Order order = orderMapper.getTotalPriceByOrderId(orderId);
        if(order.equals(null)) {
            throw new CustomLogicException(ExceptionCode.ORDER_NOT_FOUND);
        }
        if(order.getTotal_price().intValue() != amount.intValue()) {
            throw new CustomLogicException(ExceptionCode.PAYMENT_MISMATCH);
        }
        if(order.getOrder_status() == OrderStatus.CANCELED) {
            throw new CustomLogicException(ExceptionCode.ORDER_CANCELED);
        }
        return order;
    }

    public void cancelOrderStatus(String orderId) {
        Order order = orderMapper.getOrderByNanoId(orderId);
        order.setOrder_status(OrderStatus.CANCELED);
        orderMapper.updateOrderStatus(order);
    }
}

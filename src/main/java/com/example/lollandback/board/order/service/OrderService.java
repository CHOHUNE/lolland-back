package com.example.lollandback.board.order.service;

import com.example.lollandback.board.order.domain.Order;
import com.example.lollandback.board.order.domain.OrderCustomerDetails;
import com.example.lollandback.board.order.domain.OrderProductDetails;
import com.example.lollandback.board.order.dto.OrderRequestDto;
import com.example.lollandback.board.order.dto.OrderResDto;
import com.example.lollandback.board.order.exception.CustomLogicException;
import com.example.lollandback.board.order.exception.ExceptionCode;
import com.example.lollandback.board.order.mapper.OrderMapper;
import com.example.lollandback.board.product.dto.ProductAndOptionDto;
import com.example.lollandback.board.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;

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
            // 재고를 초과하지 않았는지
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
        orderMapper.saveOrder(order);
        // order 생성 후 orderProductDetails
        Long id = orderMapper.getOrderIdByNanoId(dto.getOrderId());
        if(id != null) {
            for (ProductAndOptionDto optionDto : productAndOptionDto) {
                Double price = orderMapper.getPrice(optionDto.getProduct_id());
                Double total_price = price * optionDto.getQuantity();
                OrderProductDetails productDetails = new OrderProductDetails(id, total_price, optionDto);
                orderMapper.saveProductDetails(productDetails);
            }

            // 저장 후 결제에 필요한 데이터 생성
            OrderResDto orderResDto = new OrderResDto(id, dto);
            return orderResDto;
        }

        return null;
    }

    public void tossPaymentSuccess(String paymentKey, String orderId, Long amount) {

    }

}

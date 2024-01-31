package com.example.lollandback.board.order.service;

import com.example.lollandback.board.cart.mapper.CartMapper;
import com.example.lollandback.board.order.domain.Order;
import com.example.lollandback.board.order.domain.OrderProductDetails;
import com.example.lollandback.board.order.domain.OrderStatus;
import com.example.lollandback.board.order.dto.*;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;

    @Value("${toss.pay.secretKey}")
    private String testSecretKey;

    @Value("${image.file.prefix}")
    private String urlPrefix;

    public OrderResDto createOrderInfo(Long member_id, OrderRequestDto dto) {
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
                //주문된 상품 카트에서 삭제
                cartMapper.deleteCartByMemberAndProductIds(member_id, optionDto.getProduct_id(), optionDto.getOption_id());
            }

            // 저장 후 결제에 필요한 데이터 생성
            OrderResDto orderResDto = new OrderResDto(id, dto);
            return orderResDto;
        }

        return null;
    }

    public PaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) throws JsonProcessingException {
        Order order = verifyPayment(orderId, amount);
        PaymentSuccessDto response = requestPayment(paymentKey, orderId, amount);
        order.setOrder_status(OrderStatus.ORDERED);
        orderMapper.updateOrderStatus(order); // 주문 상태 변경
        order.setPaymentKey(response.getPaymentKey());
        orderMapper.updatePaymentKey(order); // 주문 키 변경

        //주문 시각 변경
        String approvedAt = response.getApprovedAt();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(approvedAt, formatter);
        LocalDateTime update_time = offsetDateTime.toLocalDateTime();
        orderMapper.updateOrderTime(order.getId(), update_time);

        // 옵션별로 재고 빼기
        List<UpdateStockDto> updateDto = orderMapper.findAllProductOptionByOrderId(order.getId());
        for(UpdateStockDto dto : updateDto) {
            Long quantity = dto.getQuantity().longValue();
            int rows = orderMapper.subtractOptionStock(dto.getOption_id(), quantity);
            // 총 재고 빼기
            orderMapper.subtractTotalStock(dto.getProduct_id(), dto.getQuantity());
        }

        return response;
    }

    public HttpHeaders createHeader() {
        HttpHeaders headers = new HttpHeaders();
        String encodedAuthKey = new String(Base64.getEncoder().encode((testSecretKey + ":")
                .getBytes(StandardCharsets.UTF_8)));
        headers.setBasicAuth(encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return headers;
    }

    public PaymentSuccessDto requestPayment(String paymentKey, String orderId, Long amount) {
        RestTemplate restTemplate = new RestTemplate();
        // 헤더 생성
        HttpHeaders headers = createHeader();

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

    // --------- 주문 상태 변경 코드 ---------
    public void cancelOrderStatus(String orderId) {
        Order order = orderMapper.getOrderByNanoId(orderId);
        order.setOrder_status(OrderStatus.CANCELED);
        orderMapper.updateOrderStatus(order);
    }

    public void cancelWaitOrderStatus(String orderId) {
        Order order = orderMapper.getOrderByNanoId(orderId);
        order.setOrder_status(OrderStatus.CANCEL_WAIT);
        orderMapper.updateOrderStatus(order);
    }

    public Map<String, Object> fetchMyOrderInfo(Long member_id, Integer page) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        int countAll = orderMapper.countAllMyOrderInfo(member_id);

        int lastPageNumber = (countAll - 1) / 10 + 1;
        int startPageNumber = (page - 1) / 10 * 10 + 1;
        int endPageNumber = startPageNumber + 9;
        endPageNumber = Math.min(endPageNumber, lastPageNumber);
        int prevPageNumber = startPageNumber - 10;
        int nextPageNumber = endPageNumber + 1;

        pageInfo.put("currentPageNumber", page);
        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("endPageNumber", endPageNumber);
        if (prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
        }
        if (nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }

        int from = (page - 1) * 10;

        List<OrderInfoDto> orderInfo = orderMapper.fetchMyOrderInfo(from, member_id);

        for(OrderInfoDto dto : orderInfo) {
            Long product_id = orderMapper.getFirstProductId(dto.getId());
            String imgUri = orderMapper.getImgUri(product_id, urlPrefix);
            dto.setMain_img_uri(imgUri);
        }

        map.put("orderList", orderInfo);
        map.put("pageInfo", pageInfo);

        return map;
    }

    public OrderInfoDetailDto fetchOrderInfoDetail(Long orderId) {
        OrderInfoDetailDto order = orderMapper.getOrderByOrderIndex(orderId);
        order.setId(orderId);
        List<OrderedProductDto> productList = orderMapper.getOrderProductAndOptionById(orderId);
        order.setProductList(productList);

        return order;
    }


    // --------- 관리자 페이지 에서 취소 요청 목록 ---------
    public Map<String, Object> fetchCancelReqInfo(Integer page) {
        // 페이지 정보
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        int countAll = orderMapper.countCancelReqInfo();
        int nextPageNumber = page + 1;
        int prevPageNumber = page - 1;
        int lastPageNumber = (countAll - 1) / 9 + 1;

        pageInfo.put("lastPageNumber", lastPageNumber);

        if(nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }

        if(prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
        }

        int from = (page - 1) * 9;

        // 취소 요청 정보
        List<OrderCancelReqDto> orderCancelReqDto = orderMapper.fetchCancelReqInfo(from);
        for(OrderCancelReqDto dto : orderCancelReqDto) {
            Long product_id = orderMapper.getFirstProductId(dto.getId());
            String imgUri = orderMapper.getImgUri(product_id, urlPrefix);
            dto.setMain_img_uri(imgUri);
            dto.setMembersDto(orderMapper.getCancelReqMemberInfo(dto.getId()));
        }

        map.put("orderCancelReqDto", orderCancelReqDto);
        map.put("pageInfo", pageInfo);

        return map;
    }

    public PaymentCancelDto cancelRequestConfirm(String orderId) {
        Order order = orderMapper.getOrderByNanoId(orderId);
        PaymentCancelDto response = requestCancelPayment(order.getPaymentKey());
        order.setOrder_status(OrderStatus.CANCELED);
        orderMapper.updateOrderStatus(order); //주문 취소 상태로 변경

        // 주문 시각 변경
        String approvedAt = response.getApprovedAt();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(approvedAt, formatter);
        LocalDateTime update_time = offsetDateTime.toLocalDateTime();
        orderMapper.updateOrderTime(order.getId(), update_time);

        //옵션별로 재고 더하기
        List<UpdateStockDto> updateDto = orderMapper.findAllProductOptionByOrderId(order.getId());
        for(UpdateStockDto dto : updateDto) {
            Long quantity = dto.getQuantity().longValue();
            orderMapper.refillOptionStock(dto.getOption_id(), quantity);
            orderMapper.refillTotalStock(dto.getProduct_id(), dto.getQuantity());
        }

        return response;
    }

    public PaymentCancelDto requestCancelPayment(String paymentKey) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = createHeader();

        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("cancelReason", "고객이 취소를 원함");

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBodyMap, headers);

        PaymentCancelDto response = null;

        response = restTemplate.postForObject("https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel",
                requestEntity, PaymentCancelDto.class);
        return response;
    }
}

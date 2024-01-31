package com.example.lollandback.board.order.mapper;

import com.example.lollandback.board.order.domain.Order;
import com.example.lollandback.board.order.domain.OrderProductDetails;
import com.example.lollandback.board.order.dto.*;
import com.example.lollandback.member.dto.MemberDto;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    // 주문 내역 가져오기
    @Select("""
        SELECT id, order_name, total_price, order_status, order_reg_time
        FROM productorder
        WHERE member_id = #{member_id}
    """)
    List<OrderInfoDto> fetchMyOrderInfo(Long member_id);

    //첫번째 상품의 아이디만 가져오는 코드
    @Select("""
        SELECT product_id
        FROM orderproductdetails
        WHERE order_id = #{id}
        ORDER BY id ASC
        LIMIT 1
    """)
    Long getFirstProductId(Long id);

    //사진 가져오는 코드
    @Select("""
        SELECT CONCAT(#{urlPrefix}, 'lolland/product/productMainImg/', product_id, '/', SUBSTRING_INDEX(GROUP_CONCAT(DISTINCT main_img_uri ORDER BY main_img_id ASC), ',', 1))
        FROM productimg
        WHERE product_id = #{product_id}
    """)
    String getImgUri(Long product_id, String urlPrefix);

    // 주문 상세 정보 가져오기


    // 비매품 판별 논리 코드
    @Select("""
        SELECT COUNT(*) > 0 
        FROM product
        WHERE product_id = #{product_id} AND product_status = 'none'
    """)
    boolean isStatusNone(Long product_id);

    // 재고 수량 초과 판별 논리 코드
    @Select("""
        SELECT stock < #{quantityOrdered}
        FROM productoptions WHERE option_id = #{optionId}
    """)
    boolean checkStock(Long optionId, Integer quantityOrdered);

    // 상품의 가격 가져오는 코드
    @Select("""
        SELECT product_price
        FROM product
        WHERE product_id = #{product_id}
    """)
    Double getPrice(Long product_id);

    // 주문 나노 아이디로 인덱스 찾는 로직
    @Select("""
        SELECT id
        FROM productorder
        WHERE order_nano_id = #{orderId}
    """)
    Long getOrderIdByNanoId(String orderId);

    // 새 주문 내역 생성
    @Insert("""
        INSERT INTO productorder (
        order_nano_id, order_name, member_id, total_price, order_status, 
        receiver, email, phone, address, postalCode, requirement)
        VALUES (
            #{order_nano_id},
            #{order_name},
            #{member_id},
            #{total_price},
            #{order_status},
            #{receiver},
            #{email},
            #{phone},
            #{address},
            #{postalCode},
            #{requirement}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void saveOrder(Order order);

    // 주문 상세 내역 저장하는 코드
    @Insert("""
        INSERT INTO orderproductdetails (order_id, product_id, option_id, quantity, total_price)
        VALUES (
            #{order_id},
            #{product_id},
            #{option_id},
            #{quantity},
            #{total_price}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void saveProductDetails(OrderProductDetails productDetails);

    // 주문 나노 아이디로 주문 찾아오는 코드
    @Select("""
        SELECT * 
        FROM productorder
        WHERE order_nano_id = #{orderId}
    """)
    Order getTotalPriceByOrderId(String orderId);

    // 주문 나노 아이디로 주문 가져오는 코드
    @Select("""
        SELECT *
        FROM productorder
        WHERE order_nano_id = #{orderId}
    """)
    Order getOrderByNanoId(String orderId);

    // 각 상품의 재고 업데이트를 위해 주문 수량, 옵션과 상품 아이디 가져오는 코드
    @Select("""
        SELECT option_id, quantity, product_id
        FROM orderproductdetails
        WHERE order_id = #{id}
    """)
    List<UpdateStockDto> findAllProductOptionByOrderId(Long id);

    // 옵션 아이디로 재고 가져오는 코드
    @Select("""
        SELECT stock
        FROM productoptions
        WHERE option_id = #{optionId}
    """)
    Long getStockByOptionId(Long optionId);

    // ----------------- 업데이트 코드 -----------------

    // 주문 상태 변경 코드
    @Update("""
        UPDATE productorder
        SET order_status = #{order_status}
        WHERE order_nano_id = #{order_nano_id}
    """)
    void updateOrderStatus(Order order);

    // 주문 시간 변경 코드
    @Update("""
        UPDATE productorder
        SET order_reg_time = #{update_time}
        WHERE id = #{id}
    """)
    void updateOrderTime(Long id, LocalDateTime update_time);

    // 주문 재고 변경 코드 (빼기)
    @Update("""
        UPDATE productoptions
        SET stock = stock - #{quantity}
        WHERE option_id = #{option_id}
    """)
    int subtractOptionStock(Long option_id, Long quantity);

    @Update("""
        UPDATE product
        SET total_stock = total_stock - #{quantity} 
        WHERE product_id = #{product_id}
    """)
    void subtractTotalStock(Long product_id, Integer quantity);

    // 주문 재고 변경 코드 (더하기)
    @Update("""
        UPDATE productoptions
        SET stock = stock + #{quantity}
        WHERE option_id = #{option_id}
    """)
    void refillOptionStock(Long option_id, Long quantity);

    @Update("""
        UPDATE product
        SET total_stock = total_stock + #{quantity} 
        WHERE product_id = #{product_id}
    """)
    void refillTotalStock(Long product_id, Integer quantity);

    @Select("""
        SELECT member_id, order_nano_id, order_name, email, receiver, phone, address, 
        postalCode, requirement, order_status, order_reg_time, total_price
        FROM productorder
        WHERE id = #{orderId} 
    """)
    OrderInfoDetailDto getOrderByOrderIndex(Long orderId);

    @Select("""
        SELECT od.product_id, od.option_id, od.quantity, od.total_price, 
        p.product_name, op.option_name
        FROM orderproductdetails od 
        JOIN product p ON od.product_id = p.product_id
        JOIN productoptions op ON od.option_id = op.option_id
        WHERE od.order_id = #{orderId}
        ORDER BY od.product_id
    """)
    List<OrderedProductDto> getOrderProductAndOptionById(Long orderId);

    // 결제 취소 대기 중 상품 조회
    @Select("""
        SELECT id, order_name, total_price, order_status, order_reg_time
        FROM productorder
        WHERE order_status = 'CANCEL_WAIT'
        LIMIT #{from}, 9
    """)
    List<OrderCancelReqDto> fetchCancelReqInfo(Integer from);

    // 해당 주문건을 취소한 회원 정보 가져오기
    @Select("""
        SELECT m.id, m.member_login_id, m.member_email, m.member_name, m.member_phone_number 
        FROM member m JOIN productorder op ON m.id = op.member_id 
        WHERE op.id = #{orderId}
    """)
    MemberDto getCancelReqMemberInfo(Long orderId);

}

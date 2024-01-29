package com.example.lollandback.board.order.mapper;

import com.example.lollandback.board.order.domain.Order;
import com.example.lollandback.board.order.domain.OrderProductDetails;
import com.example.lollandback.board.order.dto.UpdateStockDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("""
        SELECT COUNT(*) > 0 
        FROM product
        WHERE product_id = #{product_id} AND product_status = 'none'
    """)
    boolean isStatusNone(Long product_id);


    @Select("""
        SELECT stock < #{quantityOrdered}
        FROM productoptions WHERE option_id = #{optionId}
    """)
    boolean checkStock(Long optionId, Integer quantityOrdered);

    @Select("""
        SELECT product_price
        FROM product
        WHERE product_id = #{product_id}
    """)
    Double getPrice(Long product_id);

    @Select("""
        SELECT id
        FROM productorder
        WHERE order_nano_id = #{orderId}
    """)
    Long getOrderIdByNanoId(String orderId);

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

    @Select("""
        SELECT * 
        FROM productorder
        WHERE order_nano_id = #{orderId}
    """)
    Order getTotalPriceByOrderId(String orderId);

    @Update("""
        UPDATE productorder
        SET order_status = #{order_status}
        WHERE order_nano_id = #{order_nano_id}
    """)
    void updateOrderStatus(Order order);

    @Select("""
        SELECT *
        FROM productorder
        WHERE order_nano_id = #{orderId}
    """)
    Order getOrderByNanoId(String orderId);

    @Select("""
        SELECT option_id, quantity, product_id
        FROM orderproductdetails
        WHERE order_id = #{id}
    """)
    List<UpdateStockDto> findAllProductOptionByOrderId(Long id);

    @Update("""
        UPDATE productoptions
        SET stock = stock - #{quantity}
        WHERE option_id = #{option_id}
    """)
    int subtractOptionStock(Long option_id, Long quantity);

    @Select("""
        SELECT stock
        FROM productoptions
        WHERE option_id = #{optionId}
    """)
    Long getStockByOptionId(Long optionId);

    @Update("""
        UPDATE product
        SET total_stock = total_stock - #{quantity} 
        WHERE product_id = #{product_id}
    """)
    void subtractTotalStock(Long product_id, Integer quantity);

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
}

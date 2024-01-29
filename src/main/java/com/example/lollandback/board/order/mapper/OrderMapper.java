package com.example.lollandback.board.order.mapper;

import com.example.lollandback.board.order.domain.Order;
import com.example.lollandback.board.order.domain.OrderCustomerDetails;
import com.example.lollandback.board.order.domain.OrderProductDetails;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

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
}

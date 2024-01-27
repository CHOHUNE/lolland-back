package com.example.lollandback.board.event.mapper;

import com.example.lollandback.board.product.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EventProductMapper {

    @Select("""
            <script>
            SELECT COUNT(*)
            FROM product p JOIN company co 
            ON p.company_id = co.company_id
            WHERE p.product_status = 'event'
                <trim prefix="AND (" suffix=")" prefixOverrides="OR">
                    <if test="category == 'all' or category == 'product_name'">
                        OR p.product_name LIKE #{keyword}
                    </if>
                    <if test="category == 'all' or category == 'company_name'">
                        OR co.company_name LIKE #{keyword}
                    </if>
                </trim>
            </script>
        """)
    int countEventAll(String keyword, String category);

    @Select("""
        <script>
            SELECT 
                p.product_id,
                p.product_name,
                p.product_content,
                p.product_price,
                p.total_stock,
                p.average_rate,
                p.product_reg_time,
                p.category_id,
                p.subcategory_id,
                p.company_id,
                p.member_id,
                co.company_name
            FROM product p JOIN company co 
            ON p.company_id = co.company_id
            WHERE p.product_status = 'event'
            <if test="category == 'all'">
                AND (p.product_name LIKE #{keyword} OR co.company_name LIKE #{keyword})
            </if>
            <if test="category == 'product_name'">
                AND p.product_name LIKE #{keyword}
            </if>
            <if test="category == 'company_name'">
                AND co.company_name LIKE #{keyword}
            </if>
            ORDER BY p.product_reg_time DESC
            LIMIT #{from}, 16
        </script>
        """)
    List<Product> list(int from, String keyword, String category);
}

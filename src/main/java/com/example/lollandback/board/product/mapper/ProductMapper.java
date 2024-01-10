package com.example.lollandback.board.product.mapper;

import com.example.lollandback.board.product.domain.Category;
import com.example.lollandback.board.product.domain.Product;
import com.example.lollandback.board.product.domain.SubCategory;
import com.example.lollandback.board.product.dto.CategoryDto;
import com.example.lollandback.board.product.dto.SubCategoryDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("SELECT c.category_id, c.category_name, sc.subcategory_id, sc.category_id as subcategory_category_id, sc.subcategory_name " +
            "FROM category c " +
            "LEFT JOIN subcategory sc ON c.category_id = sc.category_id " +
            "ORDER BY c.category_id, sc.subcategory_id")
    @Results({
            @Result(property = "category_id", column = "category_id"),
            @Result(property = "category_name", column = "category_name"),
            @Result(property = "subCategory", column = "subcategory_category_id",
                    javaType = List.class, many = @org.apache.ibatis.annotations.Many(select = "getSubcategories"))
    })
    List<CategoryDto> getAllCategoriesWithSub();

    @Select("SELECT subcategory_id, subcategory_name FROM subcategory WHERE category_id = #{category_id}")
    List<SubCategoryDto> getSubcategories(Long category_id);


    @Insert("""
            INSERT INTO product (category_id, subcategory_id, company_id, product_name, product_content, product_price, total_stock)
            VALUES (#{category_id}, #{subcategory_id}, #{company_id}, #{product_name}, #{product_content}, #{product_price}, #{total_stock})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "product_id")
    int insert(Product product);

    @Select("""
            SELECT *
            FROM product
            ORDER BY product_reg_time DESC
            """)
    List<Product> list();

    @Select("""
            SELECT *
            FROM product
            WHERE product_id = #{product_id}
            """)
    Product selectById(Integer product_id);

    @Select("""
        SELECT category_name
        FROM category
        WHERE category_id = #{category_id}
    """)
    String categoryById(Long category_id);

    @Select("""
        SELECT subcategory_name
        FROM subcategory
        WHERE subcategory_id = #{subcategory_id}
    """)
    String subCategoryById(Long subcategory_id);
}

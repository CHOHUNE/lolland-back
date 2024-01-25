package com.example.lollandback.board.product.mapper;

import com.example.lollandback.board.product.domain.Category;
import com.example.lollandback.board.product.domain.Company;
import com.example.lollandback.board.product.domain.Product;
import com.example.lollandback.board.product.dto.CategoryDto;
import com.example.lollandback.board.product.dto.ProductUpdateDto;
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
            INSERT INTO product (category_id, subcategory_id, company_id, product_name, product_content, product_price, member_id)
            VALUES 
                (#{category_id}, 
                #{subcategory_id}, 
                #{company_id}, 
                #{product_name}, 
                #{product_content}, 
                #{product_price}, 
                #{member_id})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "product_id")
    int insert(Product product);


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
            WHERE p.product_status = 'none'
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
    List<Product> list(Integer from, String keyword, String category);


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

    @Delete("""
            DELETE FROM product
            WHERE product_id = #{product_id}
            """)
    void deleteByProduct(Long productId);


    @Update("""
            UPDATE product
            SET
                category_id = #{category_id},
                subcategory_id = #{subcategory_id},
                product_name = #{product_name},
                product_price = #{product_price},
                product_content = #{product_content} 
            WHERE product_id = #{product_id}
            """)
    int updateById(ProductUpdateDto productDto);

    @Select("""
            <script>
            SELECT COUNT(*)
            FROM product p JOIN company co 
            ON p.company_id = co.company_id
            WHERE p.product_status = 'none'
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
    int countAll(String keyword, String category);


    @Update("""
            UPDATE product
            SET
                product_status = 'delete'
            WHERE product_id = #{product_id}
            """)
    int deleteById(Long product_id);

    @Select("""
            SELECT *
            FROM product p JOIN category c ON p.category_id = c.category_id
            WHERE p.category_id = #{category_id} AND p.product_status = 'none'
            LIMIT #{from}, 3
            """)
    List<Product> findByCategoryId(Long category_id, Integer from);

    @Select("""
                SELECT *
                FROM product p
                JOIN category c ON p.category_id = c.category_id
                JOIN subcategory sub ON p.subcategory_id = sub.subcategory_id
                WHERE p.category_id = #{category_id} 
                AND p.subcategory_id = #{subcategory_id}
                AND p.product_status = 'none'
                LIMIT #{from}, 3
            """)
    List<Product> findByCategoryIdAndSubcategoryId(Long category_id, Long subcategory_id, Integer from);


    @Select("""
                SELECT *
                FROM category
                WHERE category_id = #{categoryId}
            """)
    Category getCategoryById(Long categoryId);

    @Select("""
                SELECT *
                FROM subcategory
                WHERE category_id = #{categoryId}
            """)
    List<SubCategoryDto> getSubcategoryById(Long categoryId);

    // 메인페이지에서 카테고리 가져올때 이거 사용하면됨.
    @Select("""
                SELECT *
                FROM category
            """)
    List<Category> getAllCategories();

    @Select("""
                SELECT DISTINCT c.company_id, c.company_name
                FROM company c
                RIGHT JOIN product p ON p.company_id = c.company_id
                WHERE p.category_id = #{categoryId} AND p.subcategory_id = #{subcategoryId} AND p.product_status = "none"
            """)
    List<Company> getAllCompanies(Long categoryId, Long subcategoryId);

    @Select("""
                SELECT COUNT(*) FROM category c LEFT JOIN product p ON c.category_id = p.category_id
                WHERE c.category_id = #{category_id} AND p.product_status = 'none';
            """)
    int countCategoryProductAll(Category category);

    @Select("""
            SELECT COUNT(*) FROM category c
                 LEFT JOIN subcategory sub ON c.category_id = sub.category_id
                 LEFT JOIN product p ON sub.subcategory_id = p.subcategory_id
            WHERE c.category_id = #{category_id} AND sub.subcategory_id = #{subcategory_id};
            """)
    int countSubCategoryProductAll();

}

package com.example.lollandback.board.product.mapper;

import com.example.lollandback.board.product.domain.Category;
import com.example.lollandback.board.product.domain.Company;
import com.example.lollandback.board.product.dto.CategoryDto;
import com.example.lollandback.board.product.dto.ProductUpdateDto;
import com.example.lollandback.board.product.dto.SubCategoryDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductCompanyMapper {
    @Insert("""
            INSERT INTO company (company_id, company_name)
            VALUES (#{company_id}, #{company_name})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "company_id")
    int insert(Company company);

    @Select("""
        SELECT company_id
        FROM company
        WHERE company_name = #{company_name}
    """)
    Long getCompanyIdByName(String company_name);


    @Select("""
            SELECT company_name
            FROM company
            WHERE company_id = #{company_id}
            """)
    Company selectById(Long company_id);

    @Delete("""
            DELETE FROM company
            WHERE company_id = #{company_id}
            """)
    void deleteByCompany(Long productId);

    @Update("""
        UPDATE company
        JOIN product ON company.company_id = product.company_id
        SET 
            company.company_name = #{company_name}
        WHERE product.product_id = #{product_id}
        """)
    void updateCompany(ProductUpdateDto productUpdateDto);

    @Select("""
        SELECT COUNT(*)
        FROM review r
        LEFT JOIN product p ON r.product_id = p.product_id
        LEFT JOIN company c ON p.company_id = c.company_id
        WHERE p.company_id = #{companyId}
    """)
    Integer getTotalReview(Long companyId);

    @Select("""
        SELECT DISTINCT c.*
        FROM category c
        RIGHT JOIN product p ON p.category_id = c.category_id
        LEFT JOIN company co ON p.company_id = co.company_id
        WHERE p.company_id = #{companyId}
    """)
    List<Category> getCompanyCategory(Long companyId);

    @Select("""
        SELECT DISTINCT sub.*
        FROM subcategory sub
            RIGHT JOIN product p ON sub.subcategory_id = p.subcategory_id
            LEFT JOIN company c ON p.company_id = c.company_id
        WHERE c.company_id = #{companyId} AND sub.category_id = #{category_id}
    """)
    List<SubCategoryDto> getSubCategoryByCompany(Long companyId, Long category_id);
}

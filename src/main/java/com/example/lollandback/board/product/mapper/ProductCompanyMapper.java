package com.example.lollandback.board.product.mapper;

import com.example.lollandback.board.product.domain.Company;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProductCompanyMapper {
    @Insert("""
            INSERT INTO company (company_id, company_name)
            VALUES (#{company_id}, #{company_name})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "company_id")
    int insert(Company company);

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
}

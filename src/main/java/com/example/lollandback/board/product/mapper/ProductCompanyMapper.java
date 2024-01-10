package com.example.lollandback.board.product.mapper;

import com.example.lollandback.board.product.domain.Company;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProductCompanyMapper {
    @Insert("""
            INSERT INTO company (company_name)
            VALUES (#{company_name})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "company_id")
    int insert(Company company);

    @Select("""
            SELECT company_name
            FROM company
            WHERE company_id = #{company_id}
            """)
    Company selectById(Long company_id);
}

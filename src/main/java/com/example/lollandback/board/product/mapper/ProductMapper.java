package com.example.lollandback.board.product.mapper;

import com.example.lollandback.board.product.dto.CategoryDto;
import com.example.lollandback.board.product.dto.SubCategoryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

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

}

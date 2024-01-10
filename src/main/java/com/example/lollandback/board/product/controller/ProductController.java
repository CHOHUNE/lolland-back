package com.example.lollandback.board.product.controller;

import com.example.lollandback.board.product.dto.CategoryDto;
import com.example.lollandback.board.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/write")
    public List<CategoryDto> getCategories() {
        return productService.getAllCategories();
    }
}

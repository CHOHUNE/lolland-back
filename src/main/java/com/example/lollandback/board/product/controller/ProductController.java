package com.example.lollandback.board.product.controller;

import com.example.lollandback.board.product.domain.Company;
import com.example.lollandback.board.product.domain.Product;
import com.example.lollandback.board.product.dto.CategoryDto;
import com.example.lollandback.board.product.dto.ProductDto;
import com.example.lollandback.board.product.dto.SubCategoryDto;
import com.example.lollandback.board.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    // --------------------------- 상품 등록 시 카테고리 보여주는 로직 ---------------------------
    @GetMapping("/write")
    public List<CategoryDto> getCategories() {
        return productService.getAllCategories();
    }

    // --------------------------- 상품 등록 로직 ---------------------------
    @PostMapping("add")
    public ResponseEntity add(Product product, Company company,
                              @RequestParam(value = "mainImg[]", required = false) MultipartFile[] mainImg) throws IOException {
        if (productService.save(product, company, mainImg)) {
            return ResponseEntity.ok(product.getProduct_id());
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    // --------------------------- 상품 리스트 로직 ---------------------------
    @GetMapping("list")
    public List<Product> list() {
        return productService.list();
    }

    // --------------------------- 상품 보기 로직 ---------------------------
    @GetMapping("product_id/{product_id}")
    public ProductDto get(@PathVariable Integer product_id) {
        ProductDto productDto = productService.get(product_id);
        System.out.println("productDto = " + productDto);
        return productDto;
    }



}

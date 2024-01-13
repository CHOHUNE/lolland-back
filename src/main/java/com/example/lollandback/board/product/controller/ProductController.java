package com.example.lollandback.board.product.controller;

import com.example.lollandback.board.product.domain.Company;
import com.example.lollandback.board.product.domain.Product;
import com.example.lollandback.board.product.domain.ProductOptions;
import com.example.lollandback.board.product.dto.*;
import com.example.lollandback.board.product.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
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
                              @RequestParam(value = "option_name[]", required = false) List<String> optionNames,
                              @RequestParam(value = "mainImg[]", required = false) MultipartFile[] mainImg) throws IOException {
        if (productService.save(product, company, mainImg, optionNames)) {
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

    // --------------------------- 상품 상세옵션 보기 로직 ---------------------------
    @GetMapping("option/{product_id}")
    public ResponseEntity<List<ProductOptionsDto>> getOption(@PathVariable Integer product_id) {
        List<ProductOptionsDto> options = productService.getOptionsByProductId(product_id);
        return ResponseEntity.ok(options);
    }

    // --------------------------- 상품 삭제 로직 ---------------------------
    @DeleteMapping("remove/{product_id}")
    public void remove(@PathVariable Long product_id) {
        productService.remove(product_id);
    }

    // --------------------------- 상품 수정 로직 ---------------------------
    @PutMapping("edit")
    public ResponseEntity update(ProductUpdateDto productUpdateDto,
                                 @RequestParam(value = "options", required = false) String options,
                                 @RequestParam(value = "removeMainImgs[]", required = false) List<Integer> removeMainImg,
                                 @RequestParam(value = "newImgs[]", required = false) MultipartFile[] newImgs) throws IOException {


        ObjectMapper objectMapper = new ObjectMapper();
        List<ProductOptionsDto> details = objectMapper.readValue(options, new TypeReference<>() {});



        if (productService.update(productUpdateDto, details, removeMainImg, newImgs )) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }
}

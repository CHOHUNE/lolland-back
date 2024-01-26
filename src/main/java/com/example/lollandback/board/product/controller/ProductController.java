package com.example.lollandback.board.product.controller;

import com.example.lollandback.board.product.domain.Category;
import com.example.lollandback.board.product.domain.Company;
import com.example.lollandback.board.product.domain.Product;
import com.example.lollandback.board.product.domain.SubCategory;
import com.example.lollandback.board.product.dto.*;
import com.example.lollandback.board.product.service.ProductService;
import com.example.lollandback.member.domain.Member;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    // --------------------------- 상품 등록 & 네브 바에 카테고리 - 그에 해당하는 서브 카테고리 불러오는 로직 ---------------------------
    @GetMapping("/category")
    public List<CategoryDto> getCategories() {
        return productService.getAllCategories();
    }

    // --------------------------- 카테고리와 해당 카테고리의 서브카테고리 리턴하는 로직 ---------------------------

    @GetMapping("/category/detail/{category_id}")
    public CategoryDetailDto getCategoryInfo(@PathVariable Long category_id) {
        return productService.getCategoryDetails(category_id);
    }

    // --------------------------- 서브 카테고리 페이지 사이드 바에 필요한 정보 리턴 ---------------------------

    @GetMapping("/subcategory/detail/{category_id}/{subcategory_id}")
    public SubcategoryNavDto getSubcategoryNav(@PathVariable Long category_id, @PathVariable Long subcategory_id) {
        return productService.getSubcategoryNav(category_id, subcategory_id);
    }

    // --------------------------- 회사별 페이지 사이드 바에 필요한 정보 리턴 ---------------------------

    @GetMapping("/company/detail/{company_id}")
    public CompanyNavDto getCompanyNav(@PathVariable Long company_id) {
        return productService.getCompanyNav(company_id);
    }

    // --------------------------- 상품 등록 로직 ---------------------------
    @PostMapping("add")
    public ResponseEntity add(Product product, Company company,
                              @SessionAttribute(value = "login", required = false) Member login,
                              @RequestParam(value = "options", required = false) String options,
                              @RequestParam(value = "mainImg[]", required = false) MultipartFile[] mainImg,
                              @RequestParam(value = "contentImg[]", required = false) MultipartFile[] contentImg) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ProductOptionsDto> optionsList = objectMapper.readValue(options, new TypeReference<>() {
        });
        if (productService.save(product, login, company, mainImg, contentImg, optionsList)) {
            return ResponseEntity.ok(product.getProduct_id());
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    // --------------------------- 전체 상품 리스트 로직 ---------------------------
    @GetMapping("list")
    public Map<String, Object> list(@RequestParam(value = "p", defaultValue = "1") Integer page,
                                    @RequestParam(value = "k", defaultValue = "") String keyword,
                                    @RequestParam(value = "c", defaultValue = "all") String category) {
        return productService.list(page, keyword, category);
    }

    @GetMapping("company")
    public Map<String, Object> list(@RequestParam(value = "p", defaultValue = "1") Integer page,
                                    @RequestParam(value = "k", defaultValue = "") String keyword,
                                    @RequestParam(value = "c", defaultValue = "all") String category,
                                    @RequestParam(value = "company_id", defaultValue = "all") Long company_id) {
        return productService.companyList(page, keyword, category, company_id);
    }

    // ------------------------------- 대분류 카테고리 상품 리스트 페이징 로직 -------------------------------
    @GetMapping("/category/{category_id}")
    public Map<String, Object> getCategoryById(@PathVariable Long category_id,
                                               @RequestParam(value = "p", defaultValue = "1") Integer page) {

        return productService.findProductsByCategoryId(category_id, page);
    }

    // ------------------------------- 소분류 서브카테고리 상품 리스트 페이징 로직 -------------------------------
    @GetMapping("/category/{category_id}/{subcategory_id}")
    public Map<String, Object> getSubCategoryById(@PathVariable Long category_id, @PathVariable Long subcategory_id,
                                                  @RequestParam(value = "p", defaultValue = "1") Integer page) {
        return productService.findProductsBySubCategory(category_id, subcategory_id, page);
        //TODO: getSubCategoryById, getCategoryById의 상품 목록을 프론트로 리턴할 때 products로 통일해주세요 ex. map.put("products", products)
    }

    // --------------------------- 상품 보기 로직 ---------------------------
    @GetMapping("product_id/{product_id}")
    public ProductDto get(@PathVariable Integer product_id) {
        ProductDto productDto = productService.get(product_id);
        return productDto;
    }

    // --------------------------- 상품 상세옵션 보기 로직 ---------------------------
    @GetMapping("option/{product_id}")
    public ResponseEntity<List<ProductOptionsDto>> getOption(@PathVariable Integer product_id) {
        List<ProductOptionsDto> options = productService.getOptionsByProductId(product_id);
        return ResponseEntity.ok(options);
    }

    // --------------------------- 상품 삭제(숨김) 로직 ---------------------------
    @PutMapping("remove/{product_id}")
    public void remove(@PathVariable Long product_id) {
        productService.remove(product_id);
    }

    // --------------------------- 상품 수정 로직 ---------------------------
    @PutMapping("edit")
    public ResponseEntity update(ProductUpdateDto productUpdateDto,
                                 @RequestParam(value = "options", required = false) String options,
                                 @RequestParam(value = "removeMainImgs[]", required = false) List<Integer> removeMainImg,
                                 @RequestParam(value = "newImgs[]", required = false) MultipartFile[] newImgs,
                                 @RequestParam(value = "removeContentImgs[]", required = false) List<Integer> removeContentImg,
                                 @RequestParam(value = "newContentImgs[]", required = false) MultipartFile[] newContentImgs) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ProductOptionsDto> details = objectMapper.readValue(options, new TypeReference<>() {
        });
        if (productService.update(productUpdateDto, details, removeMainImg, newImgs, removeContentImg, newContentImgs)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }




    // ------------------------------- 메인페이지 카테고리 가져오는 로직 -------------------------------
    @GetMapping("/mainCategory")
    public List<Category> getCategory() {
        return productService.getCategoryById();
    }

    // --------------------------- 메인페이지 리뷰 많은 상품 3개 가져오는 로직 ---------------------------
    @GetMapping("/most-reviewed")
    public ResponseEntity<List<MainProductDto>> getMostReviewedProducts() {
        List<MainProductDto> products = productService.getMostReviewedProducts();
        return ResponseEntity.ok(products);
    }
}

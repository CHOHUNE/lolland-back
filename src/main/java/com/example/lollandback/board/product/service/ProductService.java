package com.example.lollandback.board.product.service;

import com.example.lollandback.board.like.service.ProductLikeService;
import com.example.lollandback.board.product.domain.*;
import com.example.lollandback.board.product.dto.*;
import com.example.lollandback.board.product.mapper.*;
import com.example.lollandback.board.review.mapper.ReviewMapper;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final S3Client s3;

    @Value("${aws.s3.bucket.name}")
    private String bucket;

    @Value("${image.file.prefix}")
    private String urlPrefix;

    private final ProductMapper productMapper;
    private final ProductCompanyMapper companyMapper;
    private final ProductMainImg mainImgMapper;
    private final ProductOptionMapper productOptionMapper;
    private final ProductContentImg contentImgMapper;
    private final ReviewMapper reviewMapper;
    private final ProductLikeService productLikeService;

    // --------------------------- 상품 저장 시 대분류/소분류 보여주기 로직 ---------------------------
    public List<CategoryDto> getAllCategories() {
        return productMapper.getAllCategoriesWithSub();
    }

    // --------------------------- 상품 저장 로직 ---------------------------
    @Transactional
    public boolean save(Product product, Member login, Company company, MultipartFile[] mainImg, MultipartFile[] contentImg, List<ProductOptionsDto> optionList) throws IOException {
        // 상품명 중복 검증
//        if (productMapper.existsByName(product.getProduct_name())) {
//            return false;
//        }
        Long total_stock = 0L;
        // 제조사 정보 저장
        if (companyMapper.insert(company) != 1) {
            return false;
        }
        product.setCompany_id(company.getCompany_id());
        if (optionList != null) {
            for (ProductOptionsDto productOptionsDto : optionList) {
                total_stock += productOptionsDto.getStock();
            }
            product.setTotal_stock(total_stock);
        }

        // 로그인된 사용자의 ID 설정
        if (login != null) {
            product.setMember_id(login.getId());
        }

        // 상품 정보 저장
        if (productMapper.insert(product) != 1) {
            return false;
        }
        // 옵션 저장 로직
        if (optionList != null) {
            for (ProductOptionsDto productOptionsDto : optionList) {
                // ProductOption 객체 생성 및 초기화
                productOptionsDto.setProduct_id(product.getProduct_id());
                productOptionMapper.insert(productOptionsDto); // 옵션 저장
            }
        }
        // 메인 이미지 정보 저장
        if (mainImg != null && mainImg.length > 0) {
            for (MultipartFile img : mainImg) {
                String imgFileName = img.getOriginalFilename();
                if (imgFileName != null && !imgFileName.isEmpty()) {
                    mainImgMapper.insert(product.getProduct_id(), imgFileName);
                    upload(product.getProduct_id(), img);
                }
            }
        }

        // 설명 이미지 정보 저장
        if (contentImg != null && contentImg.length > 0) {
            for (MultipartFile contImg : contentImg) {
                String contImgFileName = contImg.getOriginalFilename();
                if (contImgFileName != null && !contImgFileName.isBlank()) {
                    contentImgMapper.insert(product.getProduct_id(), contImgFileName);
                    upload(product.getProduct_id(), contImg);
                }
            }
        }
        return true;
    }

    // --------------------------- 이미지 업로드 로직 ---------------------------
    private void upload(Long product_id, MultipartFile mainImg) throws IOException {
        String key = "lolland/product/productMainImg/" + product_id + "/" + mainImg.getOriginalFilename();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3.putObject(objectRequest, RequestBody.fromInputStream(mainImg.getInputStream(), mainImg.getSize()));
    }

    // --------------------------- 상품 리스트 / 페이징 / 검색 로직 ---------------------------@@
    public Map<String, Object> list(Integer page, String keyword, String category) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        int countAll = productMapper.countAll("%" + keyword + "%", category);
        int lastPageNumber = (countAll - 1) / 10 + 1;
        int startPageNumber = (page - 1) / 10 * 10 + 1;
        int endPageNumber = startPageNumber + 9;
        endPageNumber = Math.min(endPageNumber, lastPageNumber);
        int prevPageNumber = startPageNumber - 10;
        int nextPageNumber = endPageNumber + 1;

        pageInfo.put("currentPageNumber", page);
        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("endPageNumber", endPageNumber);
        if (prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
        }
        if (nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }

        int from = (page - 1) * 10;

        List<Product> product = productMapper.list(from, "%" + keyword + "%", category);
        product.forEach(productListImg -> {
            List<ProductImg> productsImg = mainImgMapper.selectNamesByProductId(productListImg.getProduct_id());
            productsImg.forEach(img -> img.setMain_img_uri(urlPrefix + "lolland/product/productMainImg/" + productListImg.getProduct_id() + "/" + img.getMain_img_uri()));
            productListImg.setMainImgs(productsImg);
        });

        map.put("product", product);
        map.put("pageInfo", pageInfo);
        return map;
    }

    // --------------------------- 상품 보기 로직 ---------------------------
    public ProductDto get(Integer productId) {
        ProductDto productDto = new ProductDto();
        Product product = productMapper.selectById(productId);
        productDto.setProduct(product);

        Company company = companyMapper.selectById(product.getCompany_id());
        productDto.setCompany_name(company.getCompany_name());

        String category_name = productMapper.categoryById(product.getCategory_id());
        String subCategory_name = productMapper.subCategoryById(product.getSubcategory_id());

        productDto.setCategory_name(category_name);
        productDto.setSubcategory_name(subCategory_name);


        // 메인이미지
        List<ProductImg> productImgs = mainImgMapper.selectByProductId(Long.valueOf(productId));
        productImgs.forEach((productImg -> productImg.setMain_img_uri(urlPrefix + "lolland/product/productMainImg/" + productId + "/" + productImg.getMain_img_uri())));
        productDto.setProductImgs(productImgs);

        // 설명이미지
        List<ProductDetailsImg> detailsImgs = contentImgMapper.selectDetailsByProductId(Long.valueOf(productId));
        detailsImgs.forEach(detailsImg -> detailsImg.setSub_img_uri(urlPrefix + "lolland/product/productMainImg/" + productId + "/" + detailsImg.getSub_img_uri()));
        productDto.setProductDetailsImgs(detailsImgs);

        return productDto;
    }

    // --------------------------- 상품 상세 옵션 보기 로직 ---------------------------
    public List<ProductOptionsDto> getOptionsByProductId(Integer producId) {
        return productOptionMapper.getOptionByProductId(producId);

    }

    // --------------------------- 상품 삭제(숨김) 로직 ---------------------------
    @Transactional
    public void remove(Long productId) {
//        // 1. 메인 이미지삭제
//        deleteMainImg(productId);
//        // 2. 설명 이미지삭제
//        deleteDetailsImg(productId);
//        // 3. 옵션삭제
//        productOptionMapper.deleteByOption(productId);
//        // 4. 리뷰 삭제
//        reviewMapper.deleteReviewByProductId(productId);
//        // 5. q&a 삭제
//
//        // 6. cart 삭제
//
//        // 7. answer 삭제
//
//        // 8. productorder 삭제
//
//        // 9. 상품삭제
//        productMapper.deleteByProduct(productId);
//        // 10. 제조사 삭제
//        companyMapper.deleteByCompany(productId);

        // 찜목록 삭제
        productLikeService.removeList(productId);
        // 상품 숨김
        productMapper.deleteById(productId);

    }

    // ------------ 메인 이미지 삭제 ------------
    private void deleteMainImg(Long product_id) {
        List<ProductImg> productImgs = mainImgMapper.selectNamesByProductId(product_id);
        for (ProductImg img : productImgs) {
            String key = "lolland/product/productMainImg/" + product_id + "/" + img.getMain_img_uri();
            DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            s3.deleteObject(objectRequest);
        }
        mainImgMapper.deleteByProductId(product_id);
    }

    // ------------ 설명 이미지 삭제 ------------
    private void deleteDetailsImg(Long product_id) {
        List<ProductDetailsImg> productDetailsImgs = contentImgMapper.selectNamesByProductId(product_id);
        for (ProductDetailsImg contentImg : productDetailsImgs) {
            String key = "lolland/product/productMainImg/" + product_id + "/" + contentImg.getSub_img_uri();
            DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            s3.deleteObject(objectRequest);
        }
        contentImgMapper.deleteByProductId(product_id);
    }

    // --------------------------- 상품 수정 로직 ---------------------------
    @Transactional
    public boolean update(ProductUpdateDto productUpdateDto,
                          List<ProductOptionsDto> options,
                          List<Integer> removeMainImg,
                          MultipartFile[] newImgs,
                          List<Integer> removeContentImg,
                          MultipartFile[] newContentImg) throws IOException {
        // ------------- 메인 이미지 파일 지우기 -------------
        if (removeMainImg != null && !removeMainImg.isEmpty()) {
            for (Integer main_img_id : removeMainImg) {
                // s3삭제
                ProductImg productImg = mainImgMapper.selectById(main_img_id);
                String key = "lolland/product/productMainImg/" + main_img_id + "/" + productImg.getMain_img_uri();
                DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build();
                s3.deleteObject(objectRequest);
                // db삭제
                mainImgMapper.deleteById(main_img_id);
            }
        }
        // ------------- 새로운 메인 이미지 파일 추가 -------------
        if (newImgs != null) {
            // s3 추가
            for (MultipartFile img : newImgs) {
                upload(productUpdateDto.getProduct_id(), img);
                mainImgMapper.insert(productUpdateDto.getProduct_id(), img.getOriginalFilename());
            }
        }

        // ------------- 설명 이미지 파일 지우기 -------------
        if (removeContentImg != null && !removeContentImg.isEmpty()) {
            for (Integer details_img_id : removeContentImg) {
                // s3삭제
                ProductDetailsImg productDetailsImg = contentImgMapper.selectById(details_img_id);
                String key = "lolland/product/productMainImg/" + details_img_id + "/" + productDetailsImg.getDetails_img_id();
                DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build();
                s3.deleteObject(objectRequest);
                // db삭제
                contentImgMapper.deleteById(details_img_id);
            }
        }
        // ------------- 새로운 설명 이미지 파일 추가 -------------
        if (newContentImg != null) {
            // s3 추가
            for (MultipartFile contentImg : newContentImg) {
                upload(productUpdateDto.getProduct_id(), contentImg);
                contentImgMapper.insert(productUpdateDto.getProduct_id(), contentImg.getOriginalFilename());
            }
        }

        // ------------- 상세옵션 관련 로직 -------------
        Long total_stock = 0L;
        for (ProductOptionsDto productOptionsDto : options) {
            // 상세옵션 추가 로직
            if (productOptionsDto.getProduct_id() == null) {
                productOptionsDto.setProduct_id(productUpdateDto.getProduct_id());
                productOptionMapper.insertOptions(productOptionsDto);
            } else {
                productOptionMapper.updateOptions(productOptionsDto);
            }
            total_stock += productOptionsDto.getStock();
        }

        // ------------- 제조사 관련 로직 -------------
        companyMapper.updateCompany(productUpdateDto);

        // ------------- 상품 수정 로직 -------------
        try {
            productUpdateDto.setTotal_stock(total_stock);
            int updatedRows = productMapper.updateById(productUpdateDto);
            return updatedRows == 1;
        } catch (Exception e) {
            throw e;
        }
    }

    // --------------------------- 대분류 카테고리 리스트 & 페이징 ---------------------------!!
    public Map<String, Object> findProductsByCategoryId(Long categoryId, Integer page) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        int countAll = productMapper.countCategoryProductAll(categoryId);
        int lastPageNumber = (countAll - 1) / 3 + 1;
        int startPageNumber = (page - 1) / 3 * 3 + 1;
        int endPageNumber = startPageNumber + 2;
        endPageNumber = Math.min(endPageNumber, lastPageNumber);

        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("lastPageNumber", lastPageNumber);

        int from = (page - 1) * 3;
        List<Product> products = productMapper.findByCategoryId(categoryId, from);
        products.forEach(productListImg -> {
            List<ProductImg> productsImg = mainImgMapper.selectNamesByCategoryId(productListImg.getProduct_id());
            productsImg.forEach(img -> img.setMain_img_uri(urlPrefix + "lolland/product/productMainImg/" + productListImg.getProduct_id() + "/" + img.getMain_img_uri()));
            productListImg.setMainImgs(productsImg);
        });

        map.put("products", products);
        map.put("pageInfo", pageInfo);

        return map;
    }

    // --------------------------- 소분류 서브카테고리 리스트 & 페이징 ---------------------------
    public Map<String, Object> findProductsBySubCategory(Long category_id, Long subcategory_id, Integer page) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        int countAll = productMapper.countSubCategoryProductAll(category_id, subcategory_id);

        int lastPageNumber = (countAll - 1) / 3 + 1;
        int startPageNumber = (page - 1) / 3 * 3 + 1;
        int endPageNumber = startPageNumber + 2;
        endPageNumber = Math.min(endPageNumber, lastPageNumber);

        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("lastPageNumber", lastPageNumber);

        int from = (page - 1) * 3;

        List<Product> products = productMapper.findByCategoryIdAndSubcategoryId(category_id, subcategory_id, from);
        products.forEach(productListImg -> {
            List<ProductImg> productsImg = mainImgMapper.selectNamesByCategoryId(productListImg.getProduct_id());
            productsImg.forEach(img -> img.setMain_img_uri(urlPrefix + "lolland/product/productMainImg/" + productListImg.getProduct_id() + "/" + img.getMain_img_uri()));
            productListImg.setMainImgs(productsImg);
        });

        map.put("products", products);
        map.put("pageInfo", pageInfo);

        return map;
    }

    public CategoryDetailDto getCategoryDetails(Long categoryId) {
        Category category = productMapper.getCategoryById(categoryId);
        List<SubCategoryDto> subcategories = productMapper.getSubcategoryById(categoryId);
        CategoryDetailDto dto = new CategoryDetailDto(category, subcategories);
        return dto;
    }

    public SubcategoryNavDto getSubcategoryNav(Long categoryId, Long subcategoryId) {
        // 전체 카테고리 가져오기
        List<Category> categories = productMapper.getAllCategories();
        // 해당 카테고리 > 서브 카테고리 상품들의 distinct company 전부 가져오기
        List<Company> companies = productMapper.getAllCompanies(categoryId, subcategoryId);
        // 현재 카테고리 이름, 현재 서브 카테고리 이름 가져오기
        String category_name = productMapper.categoryById(categoryId);
        String subcategory_name = productMapper.subCategoryById(subcategoryId);

        return new SubcategoryNavDto(categories, companies, category_name, subcategory_name);
    }

    public List<Category> getCategoryById() {
        return productMapper.getAllCategories();
    }
}

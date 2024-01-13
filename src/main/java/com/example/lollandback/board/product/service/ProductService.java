package com.example.lollandback.board.product.service;

import com.example.lollandback.board.product.domain.*;
import com.example.lollandback.board.product.dto.CategoryDto;
import com.example.lollandback.board.product.dto.ProductDto;
import com.example.lollandback.board.product.dto.ProductOptionsDto;
import com.example.lollandback.board.product.dto.ProductUpdateDto;
import com.example.lollandback.board.product.mapper.*;
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
import java.util.List;

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

    // --------------------------- 상품 저장 시 대분류/소분류 보여주기 로직 ---------------------------
    public List<CategoryDto> getAllCategories() {
        return productMapper.getAllCategoriesWithSub();
    }

    // --------------------------- 상품 저장 로직 ---------------------------
    @Transactional
    public boolean save(Product product, Company company, MultipartFile[] mainImg, List<String> optionNames) throws IOException {
        // 제조사 정보 저장
        if (companyMapper.insert(company) != 1) {
            return false;
        }
        product.setCompany_id(company.getCompany_id());

        // 상품 정보 저장
        if (productMapper.insert(product) != 1) {
            return false;
        }

        // 옵션 저장 로직
        if (optionNames != null) {
            for (String optionName : optionNames) {
                // ProductOption 객체 생성 및 초기화
                ProductOptions productOption = new ProductOptions();
                productOption.setProduct_id(product.getProduct_id());
                productOption.setOption_name(optionName);
                productOptionMapper.insert(productOption); // 옵션 저장
            }
        }

        // 이미지 정보 저장
        if (mainImg != null && mainImg.length > 0) {
            for (MultipartFile img : mainImg) {
                String imgFileName = img.getOriginalFilename();
                if (imgFileName != null && !imgFileName.isEmpty()) {
                    mainImgMapper.insert(product.getProduct_id(), imgFileName);
                    upload(product.getProduct_id(), img);
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

    // --------------------------- 상품 리스트 로직 ---------------------------
    public List<Product> list() {
        List<Product> product = productMapper.list();
        product.forEach(productListImg -> {
            List<ProductImg> productsImg = mainImgMapper.selectNamesByProductId(productListImg.getProduct_id());
            productsImg.forEach(img -> img.setMain_img_uri(urlPrefix + "lolland/product/productMainImg/" + productListImg.getProduct_id() + "/" + img.getMain_img_uri()));
            productListImg.setMainImgs(productsImg);
        });
        return product;
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

        // 해당 상품 ID에 대한 이미지 URI 리스트 조회
        List<ProductImg> productImgs = mainImgMapper.selectByProductId(Long.valueOf(productId));

        // 이미지 URI 리스트를 URL로 변환하여 Product 객체에 설정
//        List<String> imgUrls = productImgs.stream()
//                .map(productImg -> urlPrefix + "lolland/product/productMainImg/" + productId + "/" + productImg.getMain_img_uri())
//                .collect(Collectors.toList());

        productImgs.forEach((productImg -> productImg.setMain_img_uri(urlPrefix + "lolland/product/productMainImg/" + productId + "/" + productImg.getMain_img_uri())));
//        productDto.setMainImgUrls(imgUrls);

        productDto.setProductImgs(productImgs);

        return productDto;
    }

    // --------------------------- 상품 상세 옵션 보기 로직 ---------------------------
    public List<ProductOptionsDto> getOptionsByProductId(Integer producId) {
        return productOptionMapper.getOptionByProductId(producId);

    }

    // --------------------------- 상품 삭제 로직 ---------------------------
    public void remove(Long productId) {
        // 1. 이미지삭제
        deleteMainImg(productId);
        // 2. 옵션삭제
        productOptionMapper.deleteByOption(productId);
        // 3. 상품삭제
        productMapper.deleteByProduct(productId);
        // 3. 제조사 삭제
        companyMapper.deleteByCompany(productId);

    }

    // ------------ 이미지 삭제 ------------
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

    // --------------------------- 상품 수정 로직 ---------------------------
    @Transactional
    public boolean update(ProductUpdateDto productUpdateDto, List<ProductOptionsDto> options, List<Integer> removeMainImg, MultipartFile[] newImgs) throws IOException {
        // ------------- 이미지 파일 지우기 -------------
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
        // ------------- 새로운 이미지 파일 추가 -------------
        if (newImgs != null) {
            // s3에 추가
            for (MultipartFile img : newImgs) {
                upload(productUpdateDto.getProduct_id(), img);
                mainImgMapper.insert(productUpdateDto.getProduct_id(), img.getOriginalFilename());
            }
        }

        // ------------- 상세옵션 관련 로직 -------------
        for (ProductOptionsDto productOptionsDto : options) {
            // 상세옵션 추가 로직
            if (productOptionsDto.getProduct_id() == null) {
                productOptionMapper.insertOptions(productOptionsDto);
            } else {
                productOptionMapper.updateOptions(productOptionsDto);
            }
        }

        companyMapper.updateCompany(productUpdateDto);

        // ------------- 상품 수정 로직 -------------
        try {
            int updatedRows = productMapper.updateById(productUpdateDto);
            return updatedRows == 1;
        } catch (Exception e) {
            throw e;
        }

    }
}

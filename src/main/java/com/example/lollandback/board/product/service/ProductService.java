package com.example.lollandback.board.product.service;

import com.example.lollandback.board.product.domain.Category;
import com.example.lollandback.board.product.domain.Company;
import com.example.lollandback.board.product.domain.Product;
import com.example.lollandback.board.product.domain.ProductImg;
import com.example.lollandback.board.product.dto.CategoryDto;
import com.example.lollandback.board.product.dto.ProductDto;
import com.example.lollandback.board.product.mapper.ProductCompanyMapper;
import com.example.lollandback.board.product.mapper.ProductMainImg;
import com.example.lollandback.board.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<CategoryDto> getAllCategories() {
        return productMapper.getAllCategoriesWithSub();
    }

    @Transactional
    public boolean save(Product product, Company company, MultipartFile[] mainImg) throws IOException {
        // 제조사 정보 저장
        if (companyMapper.insert(company) != 1) {
            return false;
        }
        product.setCompany_id(company.getCompany_id());

        // 상품 정보 저장
        if (productMapper.insert(product) != 1) {
            return false;
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

    // 이미지 업로드 로직
    private void upload(Long product_id, MultipartFile mainImg) throws IOException {
        String key = "lolland/product/productMainImg/" + product_id + "/" + mainImg.getOriginalFilename();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3.putObject(objectRequest, RequestBody.fromInputStream(mainImg.getInputStream(), mainImg.getSize()));
    }

    public List<Product> list() {
        List<Product> product = productMapper.list();
        return product;
    }


    public ProductDto get(Integer productId) {
        ProductDto productDto = new ProductDto();
        Product product = productMapper.selectById(productId);
        System.out.println("product = " + product);
        productDto.setProduct(product);

        Company company = companyMapper.selectById(product.getCompany_id());
        System.out.println("company = " + company);
        productDto.setCompany_name(company.getCompany_name());

        String category_name = productMapper.categoryById(product.getCategory_id());
        System.out.println("category_name = " + category_name);
        String subCategory_name = productMapper.subCategoryById(product.getSubcategory_id());
        System.out.println("subCategory_name = " + subCategory_name);

        productDto.setCategory_name(category_name);
        productDto.setSubcategory_name(subCategory_name);

        // 해당 상품 ID에 대한 이미지 URI 리스트 조회
        List<ProductImg> productImgs = mainImgMapper.selectByProductId(Long.valueOf(productId));

        // 이미지 URI 리스트를 URL로 변환하여 Product 객체에 설정
        List<String> imgUrls = productImgs.stream()
                .map(productImg -> urlPrefix + "lolland/product/productMainImg/" + productId + "/" + productImg.getMain_img_uri())
                .collect(Collectors.toList());
        productDto.setMainImgUrls(imgUrls);

        return productDto;
    }
}

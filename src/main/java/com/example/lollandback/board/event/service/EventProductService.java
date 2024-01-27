package com.example.lollandback.board.event.service;

import com.example.lollandback.board.event.mapper.EventProductMapper;
import com.example.lollandback.board.product.domain.Product;
import com.example.lollandback.board.product.domain.ProductImg;
import com.example.lollandback.board.product.mapper.ProductMainImg;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventProductService {

    @Value("${image.file.prefix}")
    private String urlPrefix;

    private final EventProductMapper mapper;
    private final ProductMainImg mainImgMapper;

    public Map<String, Object> getEventProduct(Integer page, String keyword, String category) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        int countAll = mapper.countEventAll("%" + keyword + "%", category);
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

        List<Product> product = mapper.list(from, "%" + keyword + "%", category);
        product.forEach(productListImg -> {
            List<ProductImg> productsImg = mainImgMapper.selectNamesByProductId(productListImg.getProduct_id());
            productsImg.forEach(img -> img.setMain_img_uri(urlPrefix + "lolland/product/productMainImg/" + productListImg.getProduct_id() + "/" + img.getMain_img_uri()));
            productListImg.setMainImgs(productsImg);
        });

        map.put("product", product);
        map.put("pageInfo", pageInfo);
        return map;

    }
}

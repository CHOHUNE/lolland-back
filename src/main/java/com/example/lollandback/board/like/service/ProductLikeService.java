package com.example.lollandback.board.like.service;

import com.example.lollandback.board.like.domain.ProductLike;
import com.example.lollandback.board.like.dto.ProductLikeDto;
import com.example.lollandback.board.like.mapper.ProductLikeMapper;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ProductLikeService {

    private final S3Client s3;

    @Value("${image.file.prefix}")
    private String urlPrefix;

    private final ProductLikeMapper productLikeMapper;

    // ---------------------------- 좋아요 눌렀을때 프론트에서 백으로 넘기는 정보 로직 ----------------------------
    public Map<String, Object> update(ProductLike productLike, Member login) {
        if (login == null || login.getId() == null) {
            throw new IllegalStateException("Member login is required");
        }
        productLike.setMember_id(login.getId());
        int count = 0;
        if (productLikeMapper.delete(productLike) == 0) {
            count = productLikeMapper.insert(productLike);
        }
        return Map.of("productLike", count == 1);
    }

    // ---------------------------- 상품마다 좋아요 눌렀을때 해당 정보를 db에 저장시키는 로직 ----------------------------
    public Map<String, Object> get(Long productId, Member login) {

        ProductLike productLike = null;
        if (login != null) {
            productLike = productLikeMapper.selectByProductIdAndMemberId(productId, login.getId());
        }
        return Map.of("productLike", productLike != null);
    }

    // ---------------------------- 상품 좋아요 목록을 찜목록으로 내보내는 로직 ----------------------------
    public List<ProductLikeDto> getAllLikes(Member login) {
        List<ProductLikeDto> productLikeDtos = productLikeMapper.selectById(login.getId(), urlPrefix);
        return productLikeDtos;
    }
}

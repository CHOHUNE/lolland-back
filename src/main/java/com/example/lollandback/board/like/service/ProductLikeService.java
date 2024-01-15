package com.example.lollandback.board.like.service;

import com.example.lollandback.board.like.domain.ProductLike;
import com.example.lollandback.board.like.mapper.ProductLikeMapper;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ProductLikeService {

    private final ProductLikeMapper productLikeMapper;

    public Map<String, Object> update(ProductLike productLike, Member login) {
        if (login == null || login.getId() == null) {
            // 오류 처리 혹은 예외 발생
            throw new IllegalStateException("Member login is required");
        }

        productLike.setMember_id(login.getId());

        int count = 0;
        if (productLikeMapper.delete(productLike) == 0) {
            count = productLikeMapper.insert(productLike);
        }
        return Map.of("productLike", count == 1);
    }

}

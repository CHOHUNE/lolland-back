package com.example.lollandback.board.like.domain;

import lombok.Data;

@Data
public class ProductLike {
    private Long like_id;
    private Long member_id;
    private Long product_id;
}

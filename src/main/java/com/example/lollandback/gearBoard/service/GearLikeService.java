package com.example.lollandback.gearBoard.service;

import com.example.lollandback.gearBoard.domain.GearLike;
import com.example.lollandback.gearBoard.mapper.GearLikeMapper;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GearLikeService {
    private  final  GearLikeMapper gearLikeMapper;

    public void update(GearLike gearLike, Member login) {
        // 처음 좋아요를 누르면 : insert
        // 다시 누르면 : delete
        gearLike.setMemberId(login.getId());

        int count=0;

        if(gearLikeMapper.delete(gearLike)==0){
            count=   gearLikeMapper.insert(gearLike);
        }

    }
}

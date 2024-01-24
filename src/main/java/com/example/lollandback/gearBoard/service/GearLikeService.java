package com.example.lollandback.gearBoard.service;

import com.example.lollandback.gearBoard.domain.GearLike;
import com.example.lollandback.gearBoard.mapper.GearLikeMapper;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GearLikeService {
    private  final  GearLikeMapper gearLikeMapper;

    public Map<String,Object> update(GearLike gearLike, Member login) {
        // 처음 좋아요를 누르면 : insert
        // 다시 누르면 : delete
        gearLike.setMemberId(login.getId());

        int count=0;

        if(gearLikeMapper.delete(gearLike)==0){
            count=   gearLikeMapper.insert(gearLike);
        }
        int countLike = gearLikeMapper.countByBoardId(gearLike.getGearboardId());
        return Map.of("gearLike", count==1,"countLike",countLike);

    }

    public Map<String, Object> get(Integer gear_id, Member login) {
        int countLike = gearLikeMapper.countByBoardId(gear_id);

        GearLike gearLike =null;
        if (login!=null){
             gearLike =  gearLikeMapper.selectbyId(gear_id,login.getId());
        }
        return  Map.of("gearLike",gearLike!=null,"countLike",countLike);
    }
}

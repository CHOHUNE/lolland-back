package com.example.lollandback.gearBoard.service;

import com.example.lollandback.gearBoard.domain.GearBoard;
import com.example.lollandback.gearBoard.domain.GearComment;
import com.example.lollandback.gearBoard.mapper.GearCommentMapper;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GearCommentService {


    private final GearCommentMapper mapper;

    public boolean add(GearComment gearComment, Member login) {
        gearComment.setMemberId(login.getId());
        return  mapper.add(gearComment)==1;

    }

    public boolean validate(GearComment gearComment) {
        if (gearComment==null){
            return false;
        }
        if (gearComment.getBoardid() ==null|| gearComment.getBoardid()<1){
            return  false;
        }
        if (gearComment.getComment()==null || gearComment.getComment().isBlank()){
            return false;
        }
        return true;
    }

    public List<GearComment> list(Integer gear_id) {
        return  mapper.list(gear_id);
    }

    public boolean remove(Integer id) {
       return mapper.remove(id)==1;
    }

    public boolean hasAccess(Integer id, Member login) {
        GearComment gearComment= mapper.selectById(id);
        return gearComment.getMemberId().equals(login.getId());

    }
}

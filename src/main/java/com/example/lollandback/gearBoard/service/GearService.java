package com.example.lollandback.gearBoard.service;


import com.example.lollandback.gearBoard.domain.GearBoard;
import com.example.lollandback.gearBoard.mapper.GearMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GearService {
    private final GearMapper mapper;


    public void save(GearBoard gearBoard) {
        mapper.save(gearBoard);
    }

    public List<GearBoard> list(String category) {
        return mapper.list(category);
    }


    public GearBoard getId(Integer gearId) {
        return  mapper.getId(gearId);
    }

    public void remove(Integer gear_id) {
       mapper.remove(gear_id);}

    public void saveup(GearBoard gearBoard) {
        mapper.saveup(gearBoard);
    }

}

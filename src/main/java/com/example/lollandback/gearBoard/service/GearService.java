package com.example.lollandback.gearBoard.service;


import com.example.lollandback.gearBoard.domain.GearBoard;
import com.example.lollandback.gearBoard.mapper.GearMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GearService {
    private final GearMapper mapper;

    public void save(GearBoard gearBoard) {
        mapper.save(gearBoard);
    }
}

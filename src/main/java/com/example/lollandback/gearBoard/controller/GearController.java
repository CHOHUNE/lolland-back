package com.example.lollandback.gearBoard.controller;

import com.example.lollandback.gearBoard.domain.GearBoard;
import com.example.lollandback.gearBoard.service.GearService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gearboard")
public class GearController {


    private final GearService service;

    @PostMapping("save")
    public  void save(@RequestBody GearBoard gearBoard){
        service.save(gearBoard);
    }




}

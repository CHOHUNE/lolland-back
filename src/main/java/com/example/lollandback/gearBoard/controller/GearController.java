package com.example.lollandback.gearBoard.controller;

import com.example.lollandback.gearBoard.domain.GearBoard;
import com.example.lollandback.gearBoard.service.GearService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gearboard")
public class GearController {


    private final GearService service;




    @PostMapping("save")
    public  void save(@RequestBody GearBoard gearBoard){
        service.save(gearBoard);
    }



//      .get("/api/gearboard/list?category=" + category)
    @GetMapping("list")
    public List<GearBoard> list(@RequestParam String category){
        return service.list(category);
    }


    @GetMapping("gear_id/{gear_id}")
    public GearBoard getId(@PathVariable Integer gear_id){
        return service.getId(gear_id);
    }

    @DeleteMapping("remove/{gear_id}")
    public void remove(@PathVariable Integer gear_id){
        service.remove(gear_id);
    }

    @PutMapping("saveup")
    public  void saveup(@RequestBody GearBoard gearBoard){
        System.out.println("gearBoard = " + gearBoard);
        service.saveup(gearBoard);
    }




}

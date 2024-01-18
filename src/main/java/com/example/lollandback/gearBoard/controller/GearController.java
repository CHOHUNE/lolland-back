package com.example.lollandback.gearBoard.controller;

import com.example.lollandback.gearBoard.domain.GearBoard;
import com.example.lollandback.gearBoard.service.GearService;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gearboard")
public class GearController {


    private final GearService service;




    @PostMapping("save")
    public ResponseEntity save(@RequestBody GearBoard gearBoard,
                               @RequestParam(value = "gear_uploadFiles[]", required = false)MultipartFile[] files,
                               @SessionAttribute(value = "login",required = false) Member login)throws  Exception{
      if (!service.validate(gearBoard)){
          return  ResponseEntity.badRequest().build();
      }

      if (service.save(gearBoard,files,login)){
          return ResponseEntity.ok().build();
      }else {
          return  ResponseEntity.internalServerError().build();
      }

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

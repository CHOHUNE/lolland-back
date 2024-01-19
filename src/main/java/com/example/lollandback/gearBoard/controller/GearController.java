package com.example.lollandback.gearBoard.controller;

import com.example.lollandback.gearBoard.domain.GearBoard;
import com.example.lollandback.gearBoard.service.GearService;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gearboard")
public class GearController {


    private final GearService service;


@PostMapping("saves")
public ResponseEntity saves(  GearBoard gearBoard,
                              @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] files,
                              @SessionAttribute(value = "login", required = false) Member login) throws IOException {
/*
  파일 넘어오는거 확인
    if (files != null) {
        for (int i = 0; i < files.length; i++) {
            System.out.println("file = " + files[i].getOriginalFilename());
            System.out.println("file.getSize() = " + files[i].getSize());
        }
    }
*/
    if (!service.validate(gearBoard)){
        return  ResponseEntity.badRequest().build();
    }
    if (service.saves(gearBoard,files,login)){
    return ResponseEntity.ok().build();
    }else{
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
    public  void saveup( GearBoard gearBoard,
                         @RequestParam(value = "removeFileIds[]",required = false) List<Integer> removeFilesIds,
                         @RequestParam(value = "uploadFiles[]",required = false) MultipartFile[] uploadFiles,
                         @SessionAttribute(value = "login" ,required = false)Member login){
        System.out.println("removeFilesIds = " + removeFilesIds);
        System.out.println("uploadFiles = " + uploadFiles);
        System.out.println("login = " + login);
        System.out.println("gearBoard = " + gearBoard);
        service.saveup(gearBoard);
    }




}

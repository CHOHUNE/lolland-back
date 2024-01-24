package com.example.lollandback.gearBoard.controller;

import com.example.lollandback.gearBoard.domain.GearComment;
import com.example.lollandback.gearBoard.service.GearCommentService;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gcomment/")
public class GearCommentController {

    private final GearCommentService commentservice;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody GearComment gearComment,
                              @SessionAttribute(value = "login",required = false)Member login){

        System.out.println("gearComment = " + gearComment);

if (commentservice.validate(gearComment)){
        if(commentservice.add(gearComment,login)){
    return  ResponseEntity.ok().build();
        }else {
            return ResponseEntity.internalServerError().build();
        }
    }else {
return ResponseEntity.badRequest().build();}
    }




    @GetMapping("list")
    public List<GearComment> list(@RequestParam("gear_id") Integer gear_id){
        return  commentservice.list(gear_id);
    }


    @DeleteMapping("remove/{id}")
    public  ResponseEntity  remove(@PathVariable Integer id,@SessionAttribute(value = "login",required = false)Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (commentservice.remove(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }


    }

}

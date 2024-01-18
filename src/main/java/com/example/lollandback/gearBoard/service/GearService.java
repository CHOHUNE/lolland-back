package com.example.lollandback.gearBoard.service;


import com.example.lollandback.gearBoard.domain.GearBoard;
//import com.example.lollandback.gearBoard.domain.GearBoardFile;
import com.example.lollandback.gearBoard.mapper.GearFileMapper;
import com.example.lollandback.gearBoard.mapper.GearMapper;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GearService {
    private final GearMapper mapper;

    private final S3Client s3;

    @Value("${aws.s3.bucket.name}")
    private String bucket;

    @Value("${image.file.prefix}")
    private String urlPrefix;

    private final GearFileMapper gearFileMapper;

/*
    public boolean save(GearBoard gearBoard,
                        MultipartFile[] files,
                        @SessionAttribute (value = "login",required = false) Member login)
            throws IOException{
        gearBoard.setMember_id(login.getMember_login_id());
        int cnt = mapper.insert(gearBoard);

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String file_url = urlPrefix + upload(gearBoard.getGear_id(), files[i]);
                gearFileMapper.insert(gearBoard.getGear_id(), files[i].getOriginalFilename(),file_url);

            }
        }
        return cnt == 1;
    }


    private String upload(Integer gear_id, MultipartFile file) throws IOException {

        String key = "lolland/gearboad/" + gear_id + "/" + file.getOriginalFilename();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return key;
    }

*/
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

    public boolean validate(GearBoard gearBoard) {
     if (gearBoard==null){
         return false;
     }
     if (gearBoard.getGear_content()==null|| gearBoard.getGear_content().isBlank()){
         return false;
     }
     if (gearBoard.getGear_title()==null|| gearBoard.getGear_title().isBlank()){
         return false;
     }
     return true;
    }


//    public GearBoard get(Integer gear_id) {
//        GearBoard gearBoard = mapper.selectById(gear_id);
//
//        List<GearBoardFile> boardFiles = gearFileMapper.selectNamesBygearboardId(gear_id);
//        gearBoard.setFiles(boardFiles);
//
//        return gearBoard;
//    }


    public boolean saves(GearBoard gearBoard, Member login) {
            gearBoard.setMember_id(login.getMember_login_id());
            return mapper.insert(gearBoard)==1;
    }
}

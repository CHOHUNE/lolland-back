package com.example.lollandback.gearBoard.service;


import com.example.lollandback.gearBoard.domain.GearBoard;
import com.example.lollandback.gearBoard.domain.GearFile;
import com.example.lollandback.gearBoard.mapper.GearCommentMapper;
import com.example.lollandback.gearBoard.mapper.GearFileMapper;
import com.example.lollandback.gearBoard.mapper.GearMapper;
import com.example.lollandback.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;


@Transactional(rollbackFor = Exception.class)
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

    public List<GearBoard> list(String category) {
        return mapper.list(category);
    }


    public GearBoard getId(Integer gear_id) {
              GearBoard board =  mapper.getId(gear_id);

              List<GearFile> gearFiles =gearFileMapper.selectNameByGearboardId(gear_id);
                for (GearFile gearFile : gearFiles){
                    String url=urlPrefix+"lolland/gearboard/"+gear_id+"/"+gearFile.getName();
                    gearFile.setUrl(url);
                }
            board.setFiles(gearFiles);
        return board;
    }

    public boolean  remove(Integer gear_id) {



        // 맴버가 작성한 댓글 삭제
        deleteFile(gear_id);
        // 첨부 파일 지우기
      return mapper.remove(gear_id)==1;
    }

    private void deleteFile(Integer gear_id) {
        //파일명조회
        List<GearFile> gearFiles= gearFileMapper.selectNameByGearboardId(gear_id);

        //s3 버켓 지우기
        for (GearFile gearFile: gearFiles){
            String key = "lolland/gearboard/" + gear_id + "/" + gearFile.getName();

            DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            s3.deleteObject(objectRequest);

        }
        //첨부파일 레코드 지우기
        gearFileMapper.deleteByGearBoardId(gear_id);
    }

    public boolean saveup(GearBoard gearBoard, List<Integer> removeFilesIds, MultipartFile[] uploadFiles) throws IOException {

        //파일 지우기
        //s3 에서 지우기

        if (removeFilesIds != null) {
            for (Integer id: removeFilesIds){
                //s3에서 지우기
                GearFile file = gearFileMapper.selectByyId(id);
                String key = "lolland/gearboard/" + gearBoard.getGear_id() + "/" + file.getName();

                DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build();
                s3.deleteObject(objectRequest);
                //db에서 지우기
                gearFileMapper.deleteById(id);
            }
        }

        //파일 추가하기
        if (uploadFiles!=null){
            for (MultipartFile file : uploadFiles){
                upload(gearBoard.getGear_id(), file);
            //db 에 추가하기
                String file_url= urlPrefix+upload(gearBoard.getGear_id(), file);
            gearFileMapper.insert(gearBoard.getGear_id(),file.getOriginalFilename(),file_url);
            }
        }

        return mapper.saveup(gearBoard)==1;
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

//    public boolean saves(GearBoard gearBoard, MultipartFile[] files, Member login) throws IOException {
//            gearBoard.setMember_id(login.getMember_login_id());
//        int cnt =  mapper.insert(gearBoard);
//
//                    // gearboardfile 테이블에 files !! 정보 저장 !!
//        if (files!=null){
//            for (int i = 0; i < files.length ; i++) {
//                    gearFileMapper.insert(gearBoard.getGear_id(), files[i].getOriginalFilename());
//                    // gearboardId, name ,id (pk) 정보만 저장
//                   //파일 을 버켓에 업로드 한다.
//                    upload(gearBoard.getGear_id(), files[i]);
//            }
//        }
//            return cnt==1;
//    }
    public boolean saves(GearBoard gearBoard, MultipartFile[] files, Member login) throws IOException {
        gearBoard.setMember_id(login.getMember_login_id());
        int cnt =  mapper.insert(gearBoard);

        // gearboardfile 테이블에 files !! 정보 저장 !!
        if (files!=null){
            for (int i = 0; i < files.length ; i++) {
                String filel_url= urlPrefix + upload(gearBoard.getGear_id(), files[i]);
                gearFileMapper.insert(gearBoard.getGear_id(), files[i].getOriginalFilename(),filel_url);
            }
        }
        return cnt==1;
    }

    private String upload(Integer gear_id,MultipartFile file) throws IOException {


        String key = "lolland/gearboard/" + gear_id + "/" + file.getOriginalFilename();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return key;

    }


//
//    private void upload(Integer gear_id,MultipartFile file) throws IOException {
//
//
//        String key = "lolland/gearboard/" + gear_id + "/" + file.getOriginalFilename();
//
//        PutObjectRequest objectRequest = PutObjectRequest.builder()
//                .bucket(bucket)
//                .key(key)
//                .acl(ObjectCannedACL.PUBLIC_READ)
//                .build();
//
//        s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
//
//    }


    public List<GearBoard> listAll() {
      return   mapper.listAll();
    }

    public List<GearBoard> listss() {
        return  mapper.listss();
    }

    public List<GearBoard> listto() {
        return  mapper.listto();
    }
}

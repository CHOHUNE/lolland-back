package com.example.lollandback.member.service;

import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.mapper.MemberImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MemberImageService {
    private final MemberImageMapper mapper;



    private final S3Client s3;

    @Value("${aws.s3.bucket.name}")
    private String bucket;

    @Value("${image.file.prefix}")
    private String urlPrefix;



    // 로그인한 유저의 이미지 수정
    public void editMemberImage(Member login, MultipartFile file, String imageType) throws IOException {


        // 새로운 이미지 정보가 들어 왔다면
        if(file != null) {
            // 이미지가 변경된다면 일단 기존 파일 이름을 갖고 온다
            String prevFileName = mapper.getPrevFileName(login.getId());

            // 기존 이미지가 S3의 경로에 존재하면 삭제하기
            String deleteKey = "lolland/user/" + login.getId() + "/" + prevFileName;
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(deleteKey)
                    .build();
            // S3 기존 이미지 파일 삭제
            s3.deleteObject(deleteObjectRequest);

            String fileUrl = urlPrefix + "lolland/user/" + login.getId() + "/" + file.getOriginalFilename();

            // 새로운 이미지로 프로필 등록 (DB에서 변경)
            mapper.editMemberImageNew(login.getId(), file.getOriginalFilename(), fileUrl);


            // 새로운 이미지 S3에 저장
            String putKey = "lolland/user/" + login.getId() + "/" + file.getOriginalFilename();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(putKey)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();
            // 파일저장 경로
            s3.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        } else {
            // 이미지 정보가 안 들어 왔을때
            if(imageType.equals("default")) {
                // 기본 이미지로 지정 했다면
                // 이미지가 변경된다면 일단 기존 파일 이름을 갖고 온다
                String prevFileName = mapper.getPrevFileName(login.getId());

                // 기존 이미지가 S3의 경로에 존재하면 삭제하기
                String deleteKey = "lolland/user/" + login.getId() + "/" + prevFileName;
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(deleteKey)
                        .build();
                // S3 기존 이미지 파일 삭제
                s3.deleteObject(deleteObjectRequest);

                // 기본 이미지로 지정 했다면
                String fileUrl = urlPrefix + "lolland/user/default/defaultImage.png";
                // 기본 프로필 이미지로 수정
                mapper.editMemberImageDefault(login.getId(), fileUrl);
            } else {
                // 사진 변경만 체크하고 아무것도 안했을 때에
                // 아무것도 하지 않는다.
            }
        }
    }
}

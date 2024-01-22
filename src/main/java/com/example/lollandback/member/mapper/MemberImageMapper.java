package com.example.lollandback.member.mapper;

import com.example.lollandback.member.dto.MemberImageDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemberImageMapper {
    // 기본 프로필로 등록
    @Insert("""
        INSERT INTO memberimage 
        (
        member_id, 
        file_name, 
        file_url, 
        image_type
        ) VALUES (
        #{id}, 
        'defaultImage.png', 
        #{fileUrl},
        'default'
        )
    """)
    void insertDefaultImage(Long id, String fileUrl);


    // 유저의 프로필 이미지 조회
    @Select("""
        SELECT * FROM memberimage
        WHERE member_id = #{id}
    """)
    MemberImageDto selectByMemberIdToImage(Long id);

    // 프로필 이미지를 새 이미지로 수정
    @Update("""
        UPDATE memberimage 
        SET 
        file_name = #{fileName},
        file_url = #{fileUrl},
        image_type = 'new'
        WHERE member_id = #{userId}
    """)
    void editMemberImageNew(Long userId, String fileName, String fileUrl);

    // 프로필 이미지를 기본 이미지로 수정
    @Update("""
        UPDATE memberimage 
        SET 
        file_name =  'defaultImage.png',
        file_url = #{fileUrl}, 
        image_type = 'default'
        WHERE member_id = #{userId}
    """)
    void editMemberImageDefault(Long userId, String fileUrl);

    // 새 이미지 전 파일명 받아오기
    @Select("""
        SELECT file_name FROM memberimage
        WHERE member_id = #{userId}
    """)
    String getPrevFileName(Long userId);
}

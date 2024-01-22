package com.example.lollandback.member.mapper;

import com.example.lollandback.member.domain.EditMember;
import com.example.lollandback.member.domain.Member;
import com.example.lollandback.member.dto.MemberDto;
import com.example.lollandback.member.dto.SetRandomPasswordDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapper {
    @Insert("""
        INSERT INTO member (
            member_login_id, 
            member_password, 
            member_name, 
            member_phone_number, 
            member_email, 
            member_type,
            member_introduce
        )
        VALUES (
            #{member_login_id},
            #{member_password},
            #{member_name},
            #{member_phone_number},
            #{member_email},
            #{member_type},
            #{member_introduce}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(Member member);

    @Select("""
        SELECT *
        FROM member 
        WHERE member_login_id = #{memberLoginId} 
    """)
    Member selectById(String memberLoginId);

    @Delete("""
        DELETE FROM member
        WHERE id = #{id}
    """)
    int deleteById(Long id);

    @Select("""
        SELECT member_login_id
        FROM member
        WHERE member_login_id =#{memberLoginId}
        """)
    MemberDto selectByLoginId(String memberLoginId);

    @Select("""
        SELECT * FROM member
        WHERE member_login_id = #{memberLoginId} AND member_password = #{password}
        """)
    String selectByLoginIdAndPassword(String memberLoginId, String password);

    @Select("""
        SELECT id, member_login_id, member_name, member_phone_number, member_email, member_type,reg_time, member_introduce FROM member
        WHERE member_login_id = #{memberLoginId}
    """)
    MemberDto selectByMemberId(String memberLoginId);

    @Select("""
        SELECT member_login_id FROM member
        WHERE member_login_id = #{memberLoginId}
    """)
    String checkUserId(String memberLoginId);

    @Select("""
        SELECT member_login_id 
        FROM member 
        WHERE member_name = #{memberName} 
        AND member_email = #{memberEmail}
    """)
    String findIdByNameAndEmail(String memberName, String memberEmail);

    @Select("""
        SELECT COUNT(*) 
        FROM member
        WHERE member_login_id = #{memberLoginId}
        AND member_email = #{memberEmail}
    """)
    Integer findUserByIdAndEmail(String memberLoginId, String memberEmail);

    @Update("""
        UPDATE member 
        SET member_password = #{member_password} 
        WHERE member_login_id = #{member_login_id}
    """)
    void setPasswordByLoginId(SetRandomPasswordDto setRandomPasswordDto);

    @Update("""
        UPDATE member 
        SET 
        member_login_id = #{member_login_id},
        member_name = #{member_name},
        member_phone_number = #{member_phone_number},
        member_email = #{member_email},
        member_introduce = #{member_introduce}
        WHERE id = #{id}
    """)
    boolean editMember(EditMember member);

    @Update("""
        UPDATE member
        SET
        member_password = #{memberPassword}
        WHERE id = #{id} 
    """)
    void editPasswordById(Long id, String memberPassword);

    // user 인 회원 10명씩 조회
    @Select("""
        SELECT * FROM member WHERE member_type = 'user'
        LIMIT #{from}, 10;
    """)
    List<MemberDto> getAllMember(Integer from);
}

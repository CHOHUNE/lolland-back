package com.example.lollandback.member.mapper;

import com.example.lollandback.member.domain.MemberAddress;
import com.example.lollandback.member.dto.MemberAddressDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberAddressMapper {

    @Insert("""
        INSERT INTO memberaddress (
        member_id,
        member_address_name, 
        member_address, 
        member_detail_address, 
        member_post_code, 
        member_address_type
        ) 
        VALUES (
            #{id},
            #{memberAddress.member_address_name},
            #{memberAddress.member_address},
            #{memberAddress.member_detail_address},
            #{memberAddress.member_post_code},
            #{memberAddress.member_address_type}
        ) 
    """)
    void insertAddress(Long id, MemberAddress memberAddress);

    @Insert("""
        INSERT INTO memberaddress (
        member_id, 
        member_address_name, 
        member_address, 
        member_detail_address, 
        member_post_code, 
        member_address_type
        ) VALUES (
        #{userId},
        #{memberAddress.member_address_name},
        #{memberAddress.member_address},
        #{memberAddress.member_detail_address},
        #{memberAddress.member_post_code},
        #{memberAddress.member_address_type}
        )
    """)
    void addAddress(MemberAddress memberAddress, Long userId);

    @Select("""
        SELECT * FROM memberaddress
        WHERE member_id = #{memberId}
        """)
    List<MemberAddressDto> getAddressByLoginUser(Long memberId);

    @Delete("""
        DELETE FROM memberaddress
        WHERE member_id = #{memberId} AND id = #{addressId}
        """)
    Integer deleteAddressByMemberAndAddressId(Long memberId, Long addressId);

    @Select("""
        SELECT ma.id, ma.member_id, 
                ma.member_address_name, ma.member_address, 
                ma.member_detail_address, ma.member_post_code, 
                ma.member_address_type, ma.reg_time
         FROM memberaddress ma JOIN member m
            ON ma.member_id = m.id
         WHERE m.member_login_id = #{memberLoginId} AND ma.member_address_type = 'main'
        """)
    MemberAddressDto selectByMemberIdToMainAddress(String memberLoginId);

    // main 주소 sub로 강등 시키기
    @Update("""
        UPDATE memberaddress ma
        JOIN member m ON ma.member_id = m.id
        SET ma.member_address_type = 'sub'
        WHERE m.id = #{userId} AND ma.member_address_type = 'main'
    """)
    void changeMainAddressType(Long userId);


    @Select("""
        SELECT COUNT(*) FROM memberaddress 
        WHERE id = #{addressId} AND member_id = #{memberId} AND member_address_type = 'main'
    """)
    int getMainAddressByMemberIdAndAddressId(Long memberId, Long addressId);

    @Update("""
        UPDATE memberaddress 
        SET 
        member_address_name = #{member_address_name},
        member_address = #{member_address},
        member_detail_address = #{member_detail_address},
        member_post_code = #{member_post_code},
        member_address_type = #{member_address_type}
        WHERE id = #{id}
    """)
    void editAddress(MemberAddress memberAddress);
}

package com.example.lollandback.member.mapper;

import com.example.lollandback.member.domain.MemberAddress;
import com.example.lollandback.member.dto.MemberAddressDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

}

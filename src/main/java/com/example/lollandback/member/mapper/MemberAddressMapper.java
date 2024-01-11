package com.example.lollandback.member.mapper;

import com.example.lollandback.member.domain.MemberAddress;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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
}

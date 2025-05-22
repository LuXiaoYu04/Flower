package com.flowers.shopping.mapper;

import com.flowers.shopping.entity.Address;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AddressMapper {
    @Insert("insert into addresses(user_id,receiver,phone,province,city,district,detail_address,is_default) values(#{userId},#{receiver},#{phone},#{province},#{city},#{district},#{detailAddress},#{isDefault})")
    boolean add(Address address);

    @Insert("update addresses set user_id=#{userId},receiver=#{receiver},phone=#{phone},province=#{province},city=#{city},district=#{district},detail_address=#{detailAddress},is_default=#{isDefault} where id=#{id}")
    boolean update(Address address);

    @Insert("delete from addresses where id=#{id}")
    boolean delete(Long id);

    @Select("select * from addresses where id=#{id}")
    Address selectById(Long id);

    @Select("select * from addresses where user_id=#{userId}")
    List<Address> selectByUserId(Long userId);
}

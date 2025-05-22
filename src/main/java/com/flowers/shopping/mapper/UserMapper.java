package com.flowers.shopping.mapper;

import com.flowers.shopping.entity.User;
import com.flowers.shopping.entity.UserQueryParam;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("insert into users(username,password,email,phone) values(#{username},#{password},#{email},#{phone})")
    boolean add(User user);

    @Select("select * from users where username=#{username}")
    User selectByUsername(String username);

    @Select("select * from users where id=#{id}")
    User selectById(Long id);

    List<User> selectByPage(UserQueryParam userQueryParam);

    boolean update(User user);

    @Delete("delete from users where id=#{id}")
    boolean delete(Long id);

    @Select("select * from users where username=#{username}")
    User getUsernameAndPassword(User user);
}

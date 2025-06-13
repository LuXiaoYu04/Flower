package com.flowers.shopping.mapper;

import com.flowers.shopping.entity.User;
import com.flowers.shopping.entity.UserQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("insert into users (username, password, email, phone, image, created_at, updated_at) values (#{username}, #{password}, #{email}, #{phone}, #{image}, #{createdAt}, #{updatedAt})")
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

    List<User> batchSelectByIds(List<Long> ids);
}

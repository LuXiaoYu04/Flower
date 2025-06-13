package com.flowers.shopping.mapper;

import com.flowers.shopping.entity.Favorite;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    @Insert("insert into favorites(user_id, products_id, create_time) values(#{userId}, #{productsId}, #{createdTime})")
    boolean add(Favorite favorite);

    @Delete("delete from favorites where id= #{id}")
    boolean delete(Long id);

    @Select("select * from favorites where user_id= #{userId}")
    List<Favorite> selectByUserId(Long userId);

    @Select("select * from favorites where products_id= #{productId}")
    List<Favorite> selectByProductId(Long productId);
}

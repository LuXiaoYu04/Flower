package com.flowers.shopping.mapper;

import com.flowers.shopping.entity.Cart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {
    @Insert("insert into cart(user_id,product_id,quantity,added_at) values(#{userId},#{productId},#{quantity},#{addedAt})")
    boolean add(Cart cart);

    boolean delete(List<Cart> cart);

    @Update("update cart set product_id=#{productId},quantity=#{quantity},added_at=#{addedAt} where id=#{id} and user_id=#{userId}")
    boolean updateCart(Cart cart);

    @Select("select * from cart where user_id=#{userId}")
    List<Cart> selectById(Long id);

    @Delete("delete from cart where user_id= #{id}")
    boolean clearCart(Long id);


    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND product_id = #{productId}")
    Cart selectByUserIdAndProductId(Cart cart);
}

package com.flowers.shopping.mapper;

import com.flowers.shopping.entity.Product;
import com.flowers.shopping.entity.ProductQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("select * from products where id=#{id}")
    Product selectById(Long id);

    @Insert("insert into products(name,description,price,stock,category,image_url,created_at,updated_at) values(#{name},#{description},#{price},#{stock},#{category},#{imageUrl},#{createdAt},#{updatedAt})")
    boolean add(Product product);

    boolean update(Product product);

    @Delete("delete from products where id=#{id}")
    boolean delete(Long id);

    List<Product> selectByPage(ProductQueryParam productQueryParam);

    @Select("select * from products")
    List<Product> selectAll();

    List<Product> batchSelectByIds(@Param("ids") List<Long> ids);

}

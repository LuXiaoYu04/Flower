package com.flowers.shopping.mapper;

import com.flowers.shopping.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper {
    void add(OrderItem orderItem);
}

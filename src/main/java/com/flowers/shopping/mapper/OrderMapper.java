package com.flowers.shopping.mapper;

import com.flowers.shopping.entity.Order;
import com.flowers.shopping.entity.OrderDTO;
import com.flowers.shopping.entity.OrderItem;
import com.flowers.shopping.entity.OrderQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    void add(Order order);

    List<OrderDTO> selectByUserId(OrderQueryParam orderQueryParam);

    Order selectById(Long id);

    // 更新订单信息
    int updateOrder(Order order);

    // 删除订单项
    void deleteOrderItemsByOrderId(Long orderId);

    // 批量插入订单项
    void insertOrderItems(@Param("orderItems") List<OrderItem> orderItems);

    // 根据订单ID删除订单
    int deleteOrderById(Long id);
}

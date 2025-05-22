package com.flowers.shopping.service.impl;

import com.flowers.shopping.entity.*;
import com.flowers.shopping.mapper.*;
import com.flowers.shopping.service.OrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CartMapper cartMapper;

    @Override
    @Transactional // 开启事务，确保所有操作要么全部成功，要么全部回滚
    public boolean submitOrder(List<Cart> cartItems, Address address) {
        if (cartItems == null || cartItems.isEmpty() || address == null) {
            return false; // 参数校验
        }
        Order order = new Order();
        order.setUserId(cartItems.get(0).getUserId());
        order.setAddressId(address.getId());
        order.setTotalAmount(calculateTotalAmount(cartItems));
        order.setStatus("待支付");
        order.setPaymentMethod("在线支付");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.add(order);

        // 缓存订单数据
        cacheOrder(order);

        for (Cart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductName(getProductName(cartItem.getProductId()));
            orderItem.setPrice(getProductPrice(cartItem.getProductId()));
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSubtotal(orderItem.getPrice() * orderItem.getQuantity());
            orderItemMapper.add(orderItem);

            // 缓存订单项数据
            cacheOrderItem(orderItem);

            Product product = productMapper.selectById(cartItem.getProductId());
            if (product == null || product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("库存不足");
            }
            product.setStock(product.getStock() - cartItem.getQuantity());
            productMapper.update(product);
        }
        cartMapper.delete(cartItems);

        // 清除用户订单列表缓存
        evictUserOrderCache(order.getUserId());
        return true;
    }

    private double calculateTotalAmount(List<Cart> cartItems) {
        double totalAmount = 0;
        for (Cart cartItem : cartItems) {
            Product product = productMapper.selectById(cartItem.getProductId());
            if (product != null) {
                totalAmount += product.getPrice().doubleValue() * cartItem.getQuantity();
            }
        }
        return totalAmount;
    }

    private String getProductName(Long productId) {
        Product product = productMapper.selectById(productId);
        return product != null ? product.getName() : "未知商品";
    }

    private double getProductPrice(Long productId) {
        Product product = productMapper.selectById(productId);
        return product != null ? product.getPrice().doubleValue() : 0;
    }

    @Override
    @Cacheable(value = "orderPageResult", key = "#orderQueryParam.userId + ':' + #orderQueryParam.page + ':' + #orderQueryParam.pageSize")
    public PageResult selectByUserId(OrderQueryParam orderQueryParam) {
        PageHelper.startPage(orderQueryParam.getPage(), orderQueryParam.getPageSize());
        List<OrderDTO> submitOrderDTOS = orderMapper.selectByUserId(orderQueryParam);
        Page<OrderDTO> page = (Page<OrderDTO>) submitOrderDTOS;
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    @Cacheable(value = "order", key = "#id")
    public Order selectById(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    @Transactional
    @CachePut(value = "order", key = "#orderDTO.order.id")
    public boolean update(OrderDTO orderDTO) {
        // 更新订单信息
        Order order = orderDTO.getOrder();
        int updateOrderResult = orderMapper.updateOrder(order);
        // 删除旧的订单项
        orderMapper.deleteOrderItemsByOrderId(order.getId());
        // 插入新的订单项
        List<OrderItem> orderItems = orderDTO.getOrderItems();
        if (orderItems != null && !orderItems.isEmpty()) {
            orderMapper.insertOrderItems(orderItems);
        }
        // 更新缓存
        cacheOrder(order);
        for (OrderItem orderItem : orderItems) {
            cacheOrderItem(orderItem);
        }
        // 清除用户订单列表缓存
        evictUserOrderCache(order.getUserId());
        return updateOrderResult > 0;
    }

    @Override
    @Transactional
    @CacheEvict(value = "order", key = "#id")
    public boolean delete(Long id) {
        // 删除订单项
        orderMapper.deleteOrderItemsByOrderId(id);
        // 删除订单
        int deleteOrderResult = orderMapper.deleteOrderById(id);
        // 清除用户订单列表缓存
        Order order = orderMapper.selectById(id);
        if (order != null) {
            evictUserOrderCache(order.getUserId());
        }
        return deleteOrderResult > 0;
    }

    // 辅助方法：缓存订单
    @CachePut(value = "order", key = "#order.id")
    public Order cacheOrder(Order order) {
        return order;
    }

    // 辅助方法：缓存订单项
    @CachePut(value = "orderItem", key = "#orderItem.id")
    public OrderItem cacheOrderItem(OrderItem orderItem) {
        return orderItem;
    }

    // 辅助方法：清除用户订单列表缓存
    @CacheEvict(value = "orderUser", key = "#userId")
    public void evictUserOrderCache(Long userId) {
        // 由于使用了 @CacheEvict，这里不需要手动操作缓存
    }
}
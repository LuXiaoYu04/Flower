package com.flowers.shopping.service;

import com.flowers.shopping.entity.*;

import java.util.List;

public interface OrderService {
    boolean submitOrder(List<Cart> cartItems, Address address);

    PageResult selectByUserId(OrderQueryParam orderQueryParam);

    Order selectById(Long id);

    boolean update(OrderDTO order);

    boolean delete(Long id);
}

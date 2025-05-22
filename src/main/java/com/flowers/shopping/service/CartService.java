package com.flowers.shopping.service;

import com.flowers.shopping.entity.Address;
import com.flowers.shopping.entity.Cart;

import java.util.List;

public interface CartService {
    boolean addCart(Cart cart);

    boolean deleteCart(List<Cart> cart);

    boolean updateCart(Cart cart);

    List<Cart> selectById(Long id);

    boolean clearCart(Long id);


}

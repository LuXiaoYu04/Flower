package com.flowers.shopping.service.impl;

import com.flowers.shopping.entity.Cart;
import com.flowers.shopping.mapper.CartMapper;
import com.flowers.shopping.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Override
    @CachePut(value = "cart", key = "#cart.id")
    public boolean addCart(Cart cart) {
        // 设置添加时间
        cart.setAddedAt(LocalDateTime.now());

        // 查询是否已存在该用户和商品的购物车记录
        Cart existingCart = cartMapper.selectByUserIdAndProductId(cart);

        if (existingCart != null) {
            // 如果存在，则更新数量
            existingCart.setQuantity(existingCart.getQuantity() + cart.getQuantity());
            return cartMapper.updateCart(existingCart);
        } else {
            // 如果不存在，则新增购物车记录
            return cartMapper.add(cart);
        }
    }

    @Override
    @CacheEvict(value = "cart", key = "#cart.![id]")
    public boolean deleteCart(List<Cart> cart) {
        return cartMapper.delete(cart);
    }

    @Override
    @CachePut(value = "cart", key = "#cart.id")
    public boolean updateCart(Cart cart) {
        cart.setAddedAt(LocalDateTime.now());
        return cartMapper.updateCart(cart);
    }

    @Override
    @Cacheable(value = "cartCache", key = "#id")
    public List<Cart> selectById(Long id) {
        return cartMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = "cart", key = "#id")
    public boolean clearCart(Long id) {
        return cartMapper.clearCart(id);
    }
}
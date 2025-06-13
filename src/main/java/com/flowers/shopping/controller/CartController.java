package com.flowers.shopping.controller;

import com.flowers.shopping.entity.Cart;
import com.flowers.shopping.entity.Result;
import com.flowers.shopping.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车管理
 */
@Slf4j
@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 通过id将商品加入购物车
     */
    @RequestMapping("/add")
    public Result addCart(@RequestBody Cart cart) {
        log.info("添加购物车:{}", cart);
        boolean flag = cartService.addCart(cart);
        if (flag) {
            return Result.success("添加购物车成功");
        } else {
            return Result.error("添加购物车失败");
        }
    }

    /**
     * 通过id将商品从购物车中删除
     */
    @DeleteMapping("/delete")
    public Result deleteCart(@RequestBody List<Cart> cart) {
        log.info("删除购物车:{}", cart);
        boolean flag = cartService.deleteCart(cart);
        if (flag) {
            return Result.success("删除购物车成功");
        } else {
            return Result.error("删除购物车失败");
        }
    }

    /**
     * 通过id将商品从购物车中修改
     */
    @PutMapping("/update")
    public Result updateCart(@RequestBody Cart cart) {
        log.info("修改购物车:{}", cart);
        boolean flag = cartService.updateCart(cart);
        if (flag) {
            return Result.success("修改购物车成功");
        } else {
            return Result.error("修改购物车失败");
        }
    }

    /**
     * 通过用户id查询购物车
     */
    @PostMapping("/selectById")
    public Result selectById(@RequestParam Long id) {
        log.info("查询购物车:{}", id);
        List<Cart> cart = cartService.selectById(id);
        if (cart != null) {
            return Result.success(cart);
        } else {
            return Result.error("查询购物车失败");
        }
    }

    /**
     * 通过用户id清空购物车
     */
    @PostMapping("/clearCart")
    public Result clearCart(@RequestParam Long id) {
        log.info("清空购物车:{}", id);
        boolean flag = cartService.clearCart(id);
        if (flag) {
            return Result.success("清空购物车成功");
        } else {
            return Result.error("清空购物车失败");
        }
    }



}

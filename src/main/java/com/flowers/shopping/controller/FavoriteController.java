package com.flowers.shopping.controller;

import com.flowers.shopping.entity.*;
import com.flowers.shopping.service.FavoriteService;
import com.flowers.shopping.service.ProductService;
import com.flowers.shopping.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 收藏管理
 */
@Slf4j
@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    /**
     * 添加收藏
     */
    @PostMapping("/add")
    public Result add(@RequestBody Favorite favorite) {
        log.info("添加收藏:{}", favorite);
        boolean result = favoriteService.add(favorite);
        return result ? Result.success("添加收藏成功") : Result.error("添加收藏失败");
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestParam Long id) {
        log.info("取消收藏:{}", id);
        boolean result = favoriteService.delete(id);
        return result ? Result.success("取消收藏成功") : Result.error("取消收藏失败");
    }

    /**
     * 批量取消收藏
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        log.info("批量取消收藏:{}", ids);
        for (Long id : ids) {
            boolean result = favoriteService.delete(id);
            if (!result) {
                return Result.error("批量取消收藏失败");
            }
        }
        return Result.success("批量取消收藏成功");
    }

    /**
     * 根据用户ID分页查询收藏
     */
    @PostMapping("/selectByUserId")
    public Result selectByUserId(@RequestParam Long userId,
                                 @RequestParam Integer page,
                                 @RequestParam Integer pageSize) {
        log.info("根据用户ID分页查询收藏:{}", userId);
        PageResult pageResult = favoriteService.selectByUserId(userId, page, pageSize);

        // 获取收藏列表中的商品ID
        List<Favorite> favorites = (List<Favorite>) pageResult.getRows();
        List<Long> productIds = favorites.stream()
                .map(Favorite::getProductsId)
                .collect(Collectors.toList());

        // 批量查询商品详情
        Map<Long, Product> productMap = productService.batchSelectByIds(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        // 构建返回结果：收藏+商品详情
        List<Map<String, Object>> resultList = favorites.stream()
                .map(favorite -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("favorite", favorite);
                    map.put("product", productMap.get(favorite.getProductsId()));
                    return map;
                })
                .collect(Collectors.toList());

        return Result.success(new PageResult(pageResult.getTotal(), resultList));
    }


    /**
     * 根据商品ID分页查询收藏（包含用户详情）
     */
    @PostMapping("/selectByProductId")
    public Result selectByProductId(@RequestParam Long productId,
                                    @RequestParam Integer page,
                                    @RequestParam Integer pageSize) {
        log.info("根据商品ID分页查询收藏:{}", productId);

        // 1. 分页查询收藏记录
        PageResult pageResult = favoriteService.selectByProductId(productId, page, pageSize);
        List<Favorite> favorites = (List<Favorite>) pageResult.getRows();

        // 2. 获取所有用户ID
        List<Long> userIds = favorites.stream()
                .map(Favorite::getUserId)
                .distinct()
                .collect(Collectors.toList());

        // 3. 批量查询用户详情（假设UserService有批量查询方法）
        Map<Long, User> userMap = userService.batchSelectByIds(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        // 4. 构建返回结果：收藏记录+用户详情
        List<Map<String, Object>> resultList = favorites.stream()
                .map(favorite -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("favorite", favorite);
                    map.put("user", userMap.get(favorite.getUserId()));
                    return map;
                })
                .collect(Collectors.toList());

        return Result.success(new PageResult(pageResult.getTotal(), resultList));
    }

}

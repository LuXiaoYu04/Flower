package com.flowers.shopping.controller;

import com.flowers.shopping.entity.*;
import com.flowers.shopping.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理
 */
@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 提交订单
     */
    @PostMapping("/submitOrder")
    public Result submitOrder(@RequestBody SubmitOrderDTO submitOrderDTO) {
        log.info("提交订单:{}", submitOrderDTO.getCartItems());
        boolean flag = orderService.submitOrder(submitOrderDTO.getCartItems(), submitOrderDTO.getAddress());
        if (flag) {
            return Result.success("提交订单成功");
        } else {
            return Result.error("提交订单失败");
        }
    }

    /**
     * 根据用户id分页查询订单
     */
    @GetMapping("/selectByUserId")
    public Result selectByUserId(OrderQueryParam orderQueryParam) {
        log.info("查询订单");
        orderQueryParam.setUserId(1L);
        PageResult pageResult = orderService.selectByUserId(orderQueryParam);
        return Result.success(pageResult);
    }


    /**
     * 根据订单id查询订单
     */
    @GetMapping("/selectById")
    public Result selectById(@RequestParam Long id) {
        log.info("查询订单");
        Order order = orderService.selectById(id);
        if (order!=null){
            return Result.success(order);
        } else {
            return Result.error("查询订单失败");
        }
    }


    /**
     * 根据订单id修改订单
     */
    @PutMapping("/update")
    public Result update(@RequestBody OrderDTO order) {
        log.info("修改订单:{}", order);
        boolean flag = orderService.update(order);
        if (flag) {
            return Result.success("修改订单成功");
        } else {
            return Result.error("修改订单失败");
        }
    }

    /**
     * 根据订单id删除订单
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestParam Long id) {
        log.info("删除订单");
        boolean flag = orderService.delete(id);
        if (flag) {
            return Result.success("删除订单成功");
        } else {
            return Result.error("删除订单失败");
        }
    }
}

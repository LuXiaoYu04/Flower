package com.flowers.shopping.controller;

import com.flowers.shopping.entity.Address;
import com.flowers.shopping.entity.Result;
import com.flowers.shopping.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址管理
 */
@Slf4j
@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    /**
     * 新增地址
     */
    @PutMapping("/add")
    public Result add(@RequestBody Address address) {
        log.info("新增地址:{}", address);
        boolean flag = addressService.add(address);
        if (flag) {
            return Result.success("新增地址成功");
        } else {
            return Result.error("新增地址失败");
        }
    }

    /**
     * 根据user_id和id修改地址
     */
    @PutMapping("/update")
    public Result update(@RequestBody Address address) {
        log.info("修改地址:{}", address);
        boolean flag = addressService.update(address);
        if (flag) {
            return Result.success("修改地址成功");
        } else {
            return Result.error("修改地址失败");
        }
    }

    /**
     * 根据id删除地址
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestParam Long id) {
        log.info("删除地址:{}", id);
        boolean flag = addressService.delete(id);
        if (flag) {
            return Result.success("删除地址成功");
        } else {
            return Result.error("删除地址失败");
        }
    }

    /**
     * 根据地址id查询地址
     */
    @PostMapping("/selectById")
    public Result selectById(@RequestParam Long id) {
        log.info("查询地址:{}", id);
        Address address = addressService.selectById(id);
        if (address != null) {
            return Result.success(address);
        } else {
            return Result.error("查询地址失败");
        }
    }

    /**
     * 根据user_id查询地址
     */
    @PostMapping("/selectByUserId")
    public Result selectByUserId(@RequestParam Long userId) {
        log.info("查询地址:{}", userId);
        List<Address> address = addressService.selectByUserId(userId);
        if (address != null) {
            return Result.success(address);
        } else {
            return Result.error("查询地址失败");
        }
    }
}

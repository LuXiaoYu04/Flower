package com.flowers.shopping.controller;

import com.flowers.shopping.entity.PageResult;
import com.flowers.shopping.entity.Result;
import com.flowers.shopping.entity.User;
import com.flowers.shopping.entity.UserQueryParam;
import com.flowers.shopping.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 用户管理
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 通过id查询单个用户
     */
    @PostMapping("/selectById")
    public Result selectById(@RequestParam Long id){
        log.info("查询用户:{}", id);
        User users = userService.selectById(id);
        if (users!=null) {
            return Result.success(users);
        } else {
            return Result.error("查询用户失败");
        }
    }

    /**
     * 分页查询用户
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(UserQueryParam userQueryParam){
        log.info("分页查询用户");
        PageResult pageResult = userService.selectByPage(userQueryParam);
        return Result.success(pageResult);
    }
    /**
     * 新增用户
     */
    @PostMapping("/add")
    public Result add(@RequestParam String username,
                      @RequestParam String password,
                      @RequestParam String email,
                      @RequestParam String phone,
                      @RequestParam MultipartFile image){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        if (!image.isEmpty()) {
            try {
                File uploadDir = new File("D:/upload");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String originalFilename = image.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() +
                        (originalFilename != null ?
                                originalFilename.substring(originalFilename.lastIndexOf(".")) : "");
                File dest = new File(uploadDir, fileName);
                image.transferTo(dest);
                user.setImage("D:/upload/" + fileName);
            } catch (IOException e) {
                log.error("图片上传失败", e);
                return Result.error("图片上传失败");
            }
        }
        log.info("新增用户:{}", user);
        return userService.add(user) ? Result.success() : Result.error("添加用户失败");
    }

    /**
     * 修改用户信息
     */
    @PutMapping
    public Result update(@RequestParam Long id,
                         @RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String email,
                         @RequestParam String phone,
                         @RequestParam MultipartFile image ){
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        if (!image.isEmpty()) {
            try {
                File uploadDir = new File("D:/upload");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String originalFilename = image.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() +
                        (originalFilename != null ?
                                originalFilename.substring(originalFilename.lastIndexOf(".")) : "");
                File dest = new File(uploadDir, fileName);
                image.transferTo(dest);
                user.setImage("D:/upload/" + fileName);
            } catch (IOException e) {
                log.error("图片上传失败", e);
                return Result.error("图片上传失败");
            }
        }
        log.info("修改用户:{}", user);
        return userService.update(user) ? Result.success() : Result.error("修改用户失败");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestParam Long id){
        log.info("删除用户:{}", id);
        return userService.delete(id) ? Result.success() : Result.error("删除用户失败");
    }


}

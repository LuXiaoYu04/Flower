package com.flowers.shopping.controller;

import com.flowers.shopping.entity.LoginInfo;
import com.flowers.shopping.entity.Result;
import com.flowers.shopping.entity.User;
import com.flowers.shopping.service.EmailService;
import com.flowers.shopping.service.UserService;
import com.flowers.shopping.utils.EmailVerificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 登录注册模块
 */
@Slf4j
@RestController
public class LoginAndRegisterController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private EmailService emailService;


    /**
     * 登录
     */
    @GetMapping("/login")
    public Result login(@RequestParam String username,
                        @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        log.info("登录：{}",user);
        LoginInfo loginInfo = userService.login(user);
        if (loginInfo != null){
            return Result.success(loginInfo);
        } else {
            return Result.error("用户名或密码错误");
        }
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String phone,
                           @RequestParam String email,
                           @RequestParam String code,
                           @RequestParam MultipartFile image) {
        log.info("注册用户：username={}, email={}", username, email);

        try {
            // 1. 验证验证码
            String storedCode = redisTemplate.opsForValue().get("verification_code:" + email);
            if (storedCode == null) {
                return Result.error("验证码已过期，请重新获取");
            }
            if (!storedCode.equals(code)) {
                return Result.error("验证码错误");
            }

            // 2. 校验必填字段
            if (username == null || password == null || email == null || phone == null) {
                return Result.error("用户名、密码、邮箱和手机号不能为空");
            }

            // 3. 检查用户名是否已存在
            if (userService.selectByUsername(username) != null) {
                return Result.error("用户名已存在");
            }
            // 4. 处理图片上传
            String imageUrl = null;
            if (!image.isEmpty()) {
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
                imageUrl = "D:/upload/" + fileName;
            }

            // 5. 创建用户对象
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setPhone(phone);
            user.setEmail(email);
            user.setImage(imageUrl);

            // 6. 注册用户
            boolean result = userService.add(user);
            if (result) {
                // 注册成功后删除验证码
                redisTemplate.delete("verification_code:" + email);
                return Result.success("注册成功");
            } else {
                return Result.error("注册失败");
            }
        } catch (IOException e) {
            log.error("图片上传失败", e);
            return Result.error("图片上传失败");
        } catch (Exception e) {
            log.error("注册失败", e);
            return Result.error("注册失败");
        }
    }


    // 验证码有效期（分钟）
    private static final int CODE_EXPIRE_MINUTES = 5;

    /**
     * 发送验证码
     */
    @PostMapping("/send-verification-code")
    public Result sendVerificationCode(@RequestParam String email) {
        try {
            // 生成验证码
            String code = EmailVerificationUtil.generateCode();

            // 存入Redis，设置5分钟过期
            redisTemplate.opsForValue().set(
                    "verification_code:" + email,
                    code,
                    CODE_EXPIRE_MINUTES,
                    TimeUnit.MINUTES
            );

            // 发送邮件
            String subject = "邮箱验证";
            String text = "您的验证码是：" + code;
            emailService.sendVerificationEmail(email, subject, text);

            return Result.error("验证码已发送");
        } catch (Exception e) {
            log.error("发送验证码失败", e);
            return Result.error("发送验证码失败");
        }
    }

    /**
     * 验证验证码
     */
    @PostMapping("/verify-code")
    public Result verifyCode(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String code = payload.get("code");

        Map<String, Object> response = new HashMap<>();

        // 从Redis获取验证码
        String storedCode = redisTemplate.opsForValue().get("verification_code:" + email);

        if (storedCode == null) {
            return Result.error("验证码已过期");
        }

        if (!storedCode.equals(code)) {
            return Result.error("验证码错误");
        }

        // 验证成功，删除Redis中的验证码
        redisTemplate.delete("verification_code:" + email);

        return Result.success("验证成功");
    }


}

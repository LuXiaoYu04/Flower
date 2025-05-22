package com.flowers.shopping.controller;

import com.flowers.shopping.service.EmailService;
import com.flowers.shopping.utils.EmailVerificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        String code = EmailVerificationUtil.generateCode();
        EmailVerificationUtil.storeCode(email, code);

        String subject = "邮箱验证";
        String text = "您的验证码是：" + code;
        emailService.sendVerificationEmail(email, subject, text);

        return ResponseEntity.ok("验证码已发送");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Map<String, Object>> verifyCode(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String code = payload.get("code");

        boolean isValid = EmailVerificationUtil.verifyCode(email, code);

        Map<String, Object> response = new HashMap<>();
        response.put("success", isValid);
        response.put("message", isValid ? "验证码正确" : "验证码错误");

        if (isValid) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}

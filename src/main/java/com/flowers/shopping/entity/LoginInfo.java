package com.flowers.shopping.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfo implements Serializable {
    private Long id;
    @NotBlank(message = "用户名不能为空")
    private String username;
    private String password;
    private String token;
}

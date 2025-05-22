package com.flowers.shopping.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfo implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String token;
}

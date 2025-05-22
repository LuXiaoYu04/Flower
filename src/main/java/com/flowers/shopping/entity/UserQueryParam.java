package com.flowers.shopping.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserQueryParam implements Serializable {
    private Integer page=1;
    private Integer pageSize=10;
    private String username;
    private String email;
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate begin;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate end;
}

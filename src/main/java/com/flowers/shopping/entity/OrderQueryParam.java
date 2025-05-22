package com.flowers.shopping.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderQueryParam implements Serializable {
    private Integer page=1;
    private Integer pageSize=10;
    private Long userId;
}

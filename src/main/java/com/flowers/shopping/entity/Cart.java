package com.flowers.shopping.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Cart implements Serializable {
    private Long id;
    private Long userId;
    private Long productId;
    private int quantity;
    private LocalDateTime addedAt;

}

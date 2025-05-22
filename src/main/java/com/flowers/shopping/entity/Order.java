package com.flowers.shopping.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data

public class Order implements Serializable {
    private Long id;
    private Long userId;
    private Long addressId;
    private double totalAmount;
    private String status;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItem> orderItems;
}

package com.flowers.shopping.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderItem implements Serializable {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private double price;
    private int quantity;
    private double subtotal;
    private Order order;

}

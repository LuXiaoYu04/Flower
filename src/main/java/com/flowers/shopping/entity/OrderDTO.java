package com.flowers.shopping.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderDTO implements Serializable {
    private Order order;
    private Address address;
    private List<OrderItem> orderItems;
}

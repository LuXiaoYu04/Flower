package com.flowers.shopping.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SubmitOrderDTO implements Serializable {
    private List<Cart> cartItems;
    private Address address;
}

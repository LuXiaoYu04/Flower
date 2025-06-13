package com.flowers.shopping.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Favorite {
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "products_id")
    private Long productsId;
    @Column(name = "create_time")
    private LocalDateTime createdTime;
}

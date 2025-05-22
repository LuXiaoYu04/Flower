package com.flowers.shopping.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductQueryParam implements Serializable {
    private Integer page=1;
    private Integer pageSize=10;
    private String name;
    private BigDecimal beginPrice;
    private BigDecimal endPrice;
    private String category;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime begin;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime end;
}

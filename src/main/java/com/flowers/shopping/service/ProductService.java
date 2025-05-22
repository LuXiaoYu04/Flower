package com.flowers.shopping.service;

import com.flowers.shopping.entity.PageResult;
import com.flowers.shopping.entity.Product;
import com.flowers.shopping.entity.ProductQueryParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Product selectById(Long id);

    boolean add(Product product, MultipartFile image);

    boolean update(Product product);

    boolean delete(Long id);

    PageResult selectByPage(ProductQueryParam productQueryParam);

    List<Product> selectAll();
}

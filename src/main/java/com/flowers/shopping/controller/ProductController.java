package com.flowers.shopping.controller;

import com.flowers.shopping.entity.PageResult;
import com.flowers.shopping.entity.Product;
import com.flowers.shopping.entity.ProductQueryParam;
import com.flowers.shopping.entity.Result;
import com.flowers.shopping.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * 商品管理
 */
@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * 通过id查询商品
     */
    @GetMapping("/{id}")
    public Result selectById(@PathVariable Long id) {
        log.info("查询所有商品");
        Product products = productService.selectById(id);
        return Result.success(products);
    }

    /**
     * 新增商品
     */
    @PostMapping("/add")
    public Result add(@RequestParam("name") String name,
                      @RequestParam("description") String description,
                      @RequestParam("price") BigDecimal price,
                      @RequestParam("stock") int stock,
                      @RequestParam("category") String category,
                      @RequestParam("image") MultipartFile image) {
        // 创建Product对象并设置属性
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);

        // 调用ProductService的add方法，并传递商品信息和图片
        boolean flag = productService.add(product, image);
        if (flag) {
            return Result.success("新增商品成功");
        } else {
            return Result.error("新增商品失败");
        }
    }

    /**
     * 更新商品
     */
    @PostMapping("/update")
    public Result update(@RequestParam("id") Long id,
                         @RequestParam("name") String name,
                         @RequestParam("description") String description,
                         @RequestParam("price") BigDecimal price,
                         @RequestParam("stock") int stock,
                         @RequestParam("category") String category,
                         @RequestParam("image") MultipartFile image) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);

        if (!image.isEmpty()) {
            try {
                File uploadDir = new File("D:/upload");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String originalFilename = image.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() +
                        (originalFilename != null ?
                                originalFilename.substring(originalFilename.lastIndexOf(".")) : "");
                File dest = new File(uploadDir, fileName);
                image.transferTo(dest);
                product.setImageUrl("D:/upload/" + fileName);
            } catch (IOException e) {
                log.error("图片上传失败", e);
                return Result.error("图片上传失败");
            }
        }
        log.info("更新商品:{}", product);
        boolean flag = productService.update(product);
        if (flag) {
            return Result.success("更新商品成功");
        } else {
            return Result.error("更新商品失败");
        }
    }

    /**
     * 删除商品
     */
    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable Long id) {
        log.info("删除商品:{}", id);
        return productService.delete(id) ? Result.success() : Result.error("删除商品失败");
    }

    /**
     * 分页查询商品
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(ProductQueryParam productQueryParam) {
        log.info("分页查询商品:{}", productQueryParam);
        PageResult pageResult = productService.selectByPage(productQueryParam);
        return Result.success(pageResult);
    }
}

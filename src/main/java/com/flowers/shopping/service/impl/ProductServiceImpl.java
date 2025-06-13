package com.flowers.shopping.service.impl;

import com.flowers.shopping.entity.PageResult;
import com.flowers.shopping.entity.Product;
import com.flowers.shopping.entity.ProductQueryParam;
import com.flowers.shopping.mapper.ProductMapper;
import com.flowers.shopping.service.ProductService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    @Cacheable(value = "product", key = "#id")
    public Product selectById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public boolean add(Product product, MultipartFile image) {
        // 1. 处理图片上传
        if (!image.isEmpty()) {
            try {
                // 创建上传目录（如果不存在）
                File uploadDir = new File("D:/upload");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // 生成唯一的文件名
                String originalFilename = image.getOriginalFilename();
                String fileName = null;
                if (originalFilename != null) {
                    fileName = UUID.randomUUID().toString() +
                            originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                // 保存图片到本地
                if (fileName != null) {
                    image.transferTo(new File(uploadDir, fileName));
                }

                // 设置图片URL（可存储相对路径）
                product.setImageUrl("D:/uploads/" + fileName);
                product.setCreatedAt(LocalDateTime.now());
                product.setUpdatedAt(LocalDateTime.now());
            } catch (Exception e) {
                // 日志记录
                log.error("图片上传失败：{}", e.getMessage());
                return false;
            }
        }

        return productMapper.add(product);
    }

    @Override
    @CachePut(value = "product", key = "#product.id")
    public boolean update(Product product) {
        product.setUpdatedAt(LocalDateTime.now());
        boolean result = productMapper.update(product);
        if (result) {
            // 更新成功后，更新缓存
            cacheProduct(product);
        }
        return result;
    }

    @Override
    @CacheEvict(value = "product", key = "#id")
    public boolean delete(Long id) {
        boolean result = productMapper.delete(id);
        if (result) {
            // 删除成功后，清除缓存
            evictProductCache(id);
        }
        return result;
    }

    @Override
    public PageResult selectByPage(ProductQueryParam productQueryParam) {
        PageHelper.startPage(productQueryParam.getPage(), productQueryParam.getPageSize());
        List<Product> products = productMapper.selectByPage(productQueryParam);
        Page<Product> page = (Page<Product>) products;
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<Product> selectAll() {
        return productMapper.selectAll();
    }

    @Override
    public List<Product> batchSelectByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return productMapper.batchSelectByIds(ids);
    }


    // 辅助方法：缓存产品
    private void cacheProduct(Product product) {
        // 由于使用了 @CachePut，这里不需要手动操作缓存
    }

    // 辅助方法：清除产品缓存
    private void evictProductCache(Long id) {
        // 由于使用了 @CacheEvict，这里不需要手动操作缓存
    }
}
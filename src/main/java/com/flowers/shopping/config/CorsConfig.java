package com.flowers.shopping.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/products/add") // 只对 /products 接口开启CORS
                .allowedOrigins("http://localhost:63342") // 前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 支持的请求方法
                .allowedHeaders("*"); // 所有头都允许

        registry.addMapping("/api/email/**") // 只对 /products 接口开启CORS
                .allowedOrigins("http://localhost:63342") // 前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 支持的请求方法
                .allowedHeaders("*"); // 所有头都允许
    }
}

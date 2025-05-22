package com.flowers.shopping.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flowers.shopping.entity.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    /**
     * 只是给 Redis 用的 ObjectMapper：注册 JavaTimeModule，
     * 关闭 WRITE_DATES_AS_TIMESTAMPS，保证 LocalDateTime/LocalDate 能序列化。
     */
    @Bean
    public ObjectMapper redisJacksonObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return om;
    }

    /**
     * 如果你在项目里还用到 RedisTemplate，就给它也打上同样的序列化器。
     * （可选：只要你走 Spring Cache，RedisTemplate 这一段可以删掉。）
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory factory,
            @Qualifier("redisJacksonObjectMapper") ObjectMapper mapper) {
        RedisTemplate<String, Object> tpl = new RedisTemplate<>();
        tpl.setConnectionFactory(factory);

        StringRedisSerializer keySer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer valSer =
                new GenericJackson2JsonRedisSerializer(mapper);

        tpl.setKeySerializer(keySer);
        tpl.setHashKeySerializer(keySer);

        tpl.setValueSerializer(valSer);
        tpl.setHashValueSerializer(valSer);

        tpl.afterPropertiesSet();
        return tpl;
    }

    /**
     * CacheManager：默认所有缓存用同一个 JSON 序列化（含 JavaTimeModule）；
     * 但针对 "userCache" 单独用 Jackson2JsonRedisSerializer<User>，
     * 这样拿回来的就是 User 对象，而不是 LinkedHashMap。
     */
    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory factory,
            @Qualifier("redisJacksonObjectMapper") ObjectMapper mapper) {

        // —— 1. 默认配置 ——
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer(mapper))
                );

        // —— 2. User 序列化器 ——
        Jackson2JsonRedisSerializer<User> userSer =
                new Jackson2JsonRedisSerializer<>(mapper, User.class);
        RedisCacheConfiguration userCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(userSer));

        // —— 3. PageResult 序列化器 ——
        Jackson2JsonRedisSerializer<PageResult> pageSer = new Jackson2JsonRedisSerializer<>(mapper, PageResult.class);
        RedisCacheConfiguration pageCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(pageSer));


        // 为 Product 类型配置反序列化器
        Jackson2JsonRedisSerializer<Product> productSer = new Jackson2JsonRedisSerializer<>(mapper, Product.class);
        RedisCacheConfiguration productCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(productSer));

        // 为 Cart 类型配置反序列化器
        Jackson2JsonRedisSerializer<Cart> cartSer = new Jackson2JsonRedisSerializer<>(mapper, Cart.class);
        RedisCacheConfiguration cartCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(cartSer));

        // 为 List<Cart> 类型配置反序列化器
        GenericJackson2JsonRedisSerializer cartListSer = new GenericJackson2JsonRedisSerializer(mapper);
        RedisCacheConfiguration cartListCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(cartListSer));

        // 为 Order 类型配置反序列化器
        Jackson2JsonRedisSerializer<Order> orderSer = new Jackson2JsonRedisSerializer<>(mapper, Order.class);
        RedisCacheConfiguration orderCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(orderSer));
        // 为 OrderItem 类型配置反序列化器
        Jackson2JsonRedisSerializer<OrderItem> orderItemSer = new Jackson2JsonRedisSerializer<>(mapper, OrderItem.class);
        RedisCacheConfiguration orderItemCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(orderItemSer));

        // 为 OrderDTO 类型配置反序列化器
        Jackson2JsonRedisSerializer<OrderDTO> orderDTOSer = new Jackson2JsonRedisSerializer<>(mapper, OrderDTO.class);
        RedisCacheConfiguration orderDTOCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(orderDTOSer));

        // 为 Address 类型配置反序列化器
        Jackson2JsonRedisSerializer<Address> addressSer = new Jackson2JsonRedisSerializer<>(mapper, Address.class);
        RedisCacheConfiguration addressCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(addressSer));

        // 为 List<Address> 类型配置反序列化器
        GenericJackson2JsonRedisSerializer addressListSer = new GenericJackson2JsonRedisSerializer(mapper);
        RedisCacheConfiguration addressListCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(addressListSer));

        // 将配置应用到 CacheManager
        Map<String, RedisCacheConfiguration> cfgMap = new HashMap<>();
        cfgMap.put("userCache", userCacheConfig);
        cfgMap.put("userPage", pageCacheConfig);
        cfgMap.put("product", productCacheConfig); // 为 Product 缓存添加配置
        cfgMap.put("cart", cartCacheConfig); // 为 Cart 缓存添加配置
        cfgMap.put("cartCache", cartListCacheConfig); // 为 List<Cart> 缓存添加配置
        cfgMap.put("order", orderCacheConfig); // 为 Order 缓存添加配置
        cfgMap.put("orderItem", orderItemCacheConfig); // 为 OrderItem 缓存添加配置
        cfgMap.put("orderUser", orderItemCacheConfig);
        cfgMap.put("orderDTO", orderDTOCacheConfig); // 为 OrderDTO 缓存添加配置
        cfgMap.put("orderPageResult", pageCacheConfig); // 为 PageResult 缓存添加配置
        cfgMap.put("address", addressCacheConfig);
        cfgMap.put("userAddress", addressListCacheConfig); // List<Address> 缓存配置


        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cfgMap)
                .build();
    }

}

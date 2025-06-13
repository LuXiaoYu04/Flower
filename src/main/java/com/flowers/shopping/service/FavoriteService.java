package com.flowers.shopping.service;

import com.flowers.shopping.entity.Favorite;
import com.flowers.shopping.entity.PageResult;

public interface FavoriteService {
    boolean add(Favorite favorite);

    boolean delete(Long id);

    PageResult selectByUserId(Long userId, Integer page, Integer pageSize);

    PageResult selectByProductId(Long productId, Integer page, Integer pageSize);
}

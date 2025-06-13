package com.flowers.shopping.service.impl;

import com.flowers.shopping.entity.Favorite;
import com.flowers.shopping.entity.PageResult;
import com.flowers.shopping.mapper.FavoriteMapper;
import com.flowers.shopping.service.FavoriteService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private FavoriteMapper favoriteMapper;

    @Override
    public boolean add(Favorite favorite) {
        favorite.setCreatedTime(LocalDateTime.now());
        return favoriteMapper.add(favorite);
    }

    @Override
    public boolean delete(Long id) {
        return favoriteMapper.delete(id);
    }

    @Override
    public PageResult selectByUserId(Long userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<Favorite> favorites = favoriteMapper.selectByUserId(userId);
        Page<Favorite> pages = (Page<Favorite>) favorites;
        return new PageResult(pages.getTotal(), pages.getResult());
    }

    @Override
    public PageResult selectByProductId(Long productId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<Favorite> favorites = favoriteMapper.selectByProductId(productId);
        Page<Favorite> pages = (Page<Favorite>) favorites;
        return new PageResult(pages.getTotal(), pages.getResult());
    }
}

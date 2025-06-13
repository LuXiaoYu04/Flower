package com.flowers.shopping.service.impl;

import com.flowers.shopping.entity.LoginInfo;
import com.flowers.shopping.entity.PageResult;
import com.flowers.shopping.entity.User;
import com.flowers.shopping.entity.UserQueryParam;
import com.flowers.shopping.mapper.UserMapper;
import com.flowers.shopping.service.UserService;
import com.flowers.shopping.utils.JwtUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginInfo login(User user) {
        User userlogin = userMapper.getUsernameAndPassword(user);
        if (userlogin != null) {
            boolean matches = passwordEncoder.matches(user.getPassword(), userlogin.getPassword());
            if (!matches) {
                return null;
            }
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("id", userlogin.getId());
            dataMap.put("username", userlogin.getUsername());
            String jwt = JwtUtils.generateJwt(dataMap);
            return new LoginInfo(userlogin.getId(), userlogin.getUsername(), userlogin.getPassword(), jwt);
        }
        return null;
    }

    @Override
    @Cacheable(value = "userCache", key = "#id")
    public User selectById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public boolean add(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User users = userMapper.selectByUsername(user.getUsername());
        if (users == null) {
            boolean result = userMapper.add(user);
            if (result) {
                // 清除分页缓存
                evictPageCache();
            }
            return result;
        } else {
            return false;
        }
    }

    @Override
    @Cacheable(value = "userPage", key = "#userQueryParam.page + ':' + #userQueryParam.pageSize")
    public PageResult<User> selectByPage(UserQueryParam userQueryParam) {
        try (Page<Object> page = PageHelper.startPage(userQueryParam.getPage(), userQueryParam.getPageSize())) {
            List<User> users = userMapper.selectByPage(userQueryParam);
            return new PageResult<>(page.getTotal(), users);
        }
    }



    @Override
    @CachePut(value = "userCache", key = "#user.id")
    public boolean update(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        boolean result = userMapper.update(user);
        if (result) {
            // 清除分页缓存
            evictPageCache();
        }
        return result;
    }

    @Override
    @CacheEvict(value = "userCache", key = "#id")
    public boolean delete(Long id) {
        boolean result = userMapper.delete(id);
        if (result) {
            // 清除分页缓存
            evictPageCache();
        }
        return result;
    }

    @Override
    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public List<User> batchSelectByIds(List<Long> ids) {
        return userMapper.batchSelectByIds(ids);
    }



    @CacheEvict(value = "userPage", allEntries = true)
    public void evictPageCache() {
        // 该方法用于清除所有分页缓存
    }
}
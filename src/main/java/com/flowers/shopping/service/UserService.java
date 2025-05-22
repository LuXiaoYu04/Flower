package com.flowers.shopping.service;

import com.flowers.shopping.entity.LoginInfo;
import com.flowers.shopping.entity.PageResult;
import com.flowers.shopping.entity.User;
import com.flowers.shopping.entity.UserQueryParam;

public interface UserService {
    LoginInfo login(User user);

    boolean add(User user);

    User selectById(Long id);

    PageResult selectByPage(UserQueryParam userQueryParam);

    boolean update(User user);

    boolean delete(Long id);


}

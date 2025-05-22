package com.flowers.shopping.service;

import com.flowers.shopping.entity.Address;

import java.util.List;

public interface AddressService {
    boolean add(Address address);

    boolean update(Address address);

    boolean delete(Long id);

    Address selectById(Long id);

    List<Address> selectByUserId(Long userId);
}

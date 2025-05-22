package com.flowers.shopping.service.impl;

import com.flowers.shopping.entity.Address;
import com.flowers.shopping.mapper.AddressMapper;
import com.flowers.shopping.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    @CacheEvict(value = "userAddress", key = "#address.userId")
    public boolean add(Address address) {
        address.setCreatedAt(LocalDateTime.now());
        address.setUpdatedAt(LocalDateTime.now());
        return addressMapper.add(address);
    }

    @Override
    @CacheEvict(value = {"address", "userAddress"}, key = "#address.id + ':' + #address.userId")
    public boolean update(Address address) {
        address.setUpdatedAt(LocalDateTime.now());
        return addressMapper.update(address);
    }

    @Override
    @CacheEvict(value = {"address", "userAddress"}, key = "#id + ':' + #userId")
    public boolean delete(Long id) {
        return addressMapper.delete(id);
    }

    @Override
    @Cacheable(value = "address", key = "#id")
    public Address selectById(Long id) {
        return addressMapper.selectById(id);
    }

    @Override
    @Cacheable(value = "userAddress", key = "#userId")
    public List<Address> selectByUserId(Long userId) {
        return addressMapper.selectByUserId(userId);
    }
}
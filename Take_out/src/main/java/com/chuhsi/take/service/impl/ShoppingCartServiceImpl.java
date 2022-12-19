package com.chuhsi.take.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.chuhsi.take.entity.ShoppingCart;
import com.chuhsi.take.mapper.ShoppingCartMapper;
import com.chuhsi.take.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}

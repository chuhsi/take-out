package com.chuhsi.take.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.chuhsi.take.entity.DishFlavor;
import com.chuhsi.take.mapper.DishFlavorMapper;
import com.chuhsi.take.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper,DishFlavor> implements DishFlavorService {
}

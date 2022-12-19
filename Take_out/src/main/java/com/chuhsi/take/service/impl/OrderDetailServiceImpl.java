package com.chuhsi.take.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.chuhsi.take.entity.OrderDetail;
import com.chuhsi.take.mapper.OrderDetailMapper;
import com.chuhsi.take.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
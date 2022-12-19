package com.chuhsi.take.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chuhsi.take.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}
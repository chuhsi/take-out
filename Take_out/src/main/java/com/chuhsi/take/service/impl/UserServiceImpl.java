package com.chuhsi.take.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuhsi.take.entity.User;
import com.chuhsi.take.mapper.UserMapper;
import com.chuhsi.take.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
}

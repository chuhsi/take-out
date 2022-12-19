package com.chuhsi.take.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuhsi.take.entity.Employee;
import com.chuhsi.take.mapper.EmployeeMapper;
import com.chuhsi.take.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}

package com.chuhsi.take.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.chuhsi.take.entity.AddressBook;
import com.chuhsi.take.mapper.AddressBookMapper;
import com.chuhsi.take.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}

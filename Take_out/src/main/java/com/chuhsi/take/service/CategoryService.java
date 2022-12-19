package com.chuhsi.take.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chuhsi.take.entity.Category;

public interface CategoryService extends IService<Category> {

    void remove(Long id);
}

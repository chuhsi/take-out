package com.chuhsi.take.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chuhsi.take.common.CustomException;
import com.chuhsi.take.entity.Category;
import com.chuhsi.take.entity.Dish;
import com.chuhsi.take.entity.Setmeal;
import com.chuhsi.take.mapper.CategoryMapper;
import com.chuhsi.take.service.CategoryService;
import com.chuhsi.take.service.DishService;
import com.chuhsi.take.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类时，需要进行判断
     *
     * @param id
     */
    @Override
    public void remove(Long id) {

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        //添加条件（菜品id关联菜品分类id）
        queryWrapper.eq(Dish::getCategoryId, id);

        //执行查询操作
        int count = dishService.count(queryWrapper);

        //查询当前分类是否关联菜品
        if (count > 0) {
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        //查询当前分类是否关联了套餐
        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId, id);
        int count1 = setmealService.count(queryWrapper1);
        if (count1 > 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //正常删除
        super.removeById(id);
    }
}

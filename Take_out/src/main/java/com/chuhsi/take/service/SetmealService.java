package com.chuhsi.take.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chuhsi.take.dto.SetmealDto;
import com.chuhsi.take.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐时，需要保存套餐和菜品的关联关系
     *
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐时，需要删除套餐和菜品的关系数据
     *
     * @param ids
     */
    void removeWithDish(List<Long> ids);
}

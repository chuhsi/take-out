package com.chuhsi.take.dto;

import com.chuhsi.take.entity.Setmeal;
import com.chuhsi.take.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

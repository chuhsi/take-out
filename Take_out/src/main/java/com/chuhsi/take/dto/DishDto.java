package com.chuhsi.take.dto;

import com.chuhsi.take.entity.Dish;
import com.chuhsi.take.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    //添加口味
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}

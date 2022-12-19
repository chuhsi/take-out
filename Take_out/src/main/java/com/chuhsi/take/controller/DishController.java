package com.chuhsi.take.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chuhsi.take.common.R;
import com.chuhsi.take.dto.DishDto;
import com.chuhsi.take.entity.Category;
import com.chuhsi.take.entity.Dish;
import com.chuhsi.take.entity.DishFlavor;
import com.chuhsi.take.service.CategoryService;
import com.chuhsi.take.service.DishFlavorService;
import com.chuhsi.take.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 菜品分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        //分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        //条件：菜品名称查询
        queryWrapper.like(name != null, Dish::getName, name);

        //条件：根据更新时间排序
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            //把集合中每一项的元数据拷贝给dishDto（数据拷贝）
            BeanUtils.copyProperties(item, dishDto);

            //从菜品中获取类品id
            Long categoryId = item.getCategoryId();

            //有id获取category对象
            Category category = categoryService.getById(categoryId);

            //设置菜品分类名称
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        //拷贝结束
        dishDtoPage.setRecords(list);

        //返回执行结果
        return R.success(dishDtoPage);
    }

    /**
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        //清除所有菜品的缓存数据
        //Set keys = redisTemplate.keys("dish_*");
        //redisTemplate.delete(keys);

        //精确清除菜品的缓存数据
        //String key = "dish_" + dishDto.getCategoryId() + "_" + dishDto.getStatus();
        //redisTemplate.delete(key);

        return R.success("新增菜品成功");
    }

    /**
     * 获取菜品数据，用于回显
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 更新菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());

        //清除所有菜品的缓存数据
        //Set keys = redisTemplate.keys("dish_*");
        //redisTemplate.delete(keys);

        //精确清除菜品的缓存数据
        String key = "dish_" + dishDto.getCategoryId() + "_" + dishDto.getStatus();
        redisTemplate.delete(key);

        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功");
    }

    /**
     * 停售处理（不支持批量）
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus1(@PathVariable int status, @RequestParam Long ids) {
        Dish dish = new Dish();
        dish.setId(ids);
        dish.setStatus(status);
        dishService.updateById(dish);
        return R.success("状态更新成功");
    }

    /**
     * 删除菜品（支持批量删除）
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        for (Long id : ids) {
            dishService.removeById(id);
        }
        return R.success("状态更新成功");
    }

    /**
     * 移动端菜品列表
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {

        List<DishDto> dishDtoList = null;

        //设置key构造规则
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();


        //1-> 先从redis中查询数据
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);

        //2-> 如果redis中存储有数据，则直接返回
        if (dishDtoList != null) {
            return R.success(dishDtoList);
        }

        //3-> redis中不存有数据，则查询mysql
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());

        //添加条件，查询状态为1（起售）的菜品
        queryWrapper.eq(Dish::getStatus, 1);

        queryWrapper.orderByAsc(Dish::getSort);

        List<Dish> list = dishService.list(queryWrapper);

        //遍历处理  list
        dishDtoList = list.stream().map((item) -> {

            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            //分类id
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());

        //4-> 若没有，则从数据库查询，再缓存到redis中
        redisTemplate.opsForValue().set(key, dishDtoList, 6, TimeUnit.HOURS);

        return R.success(dishDtoList);
    }
}

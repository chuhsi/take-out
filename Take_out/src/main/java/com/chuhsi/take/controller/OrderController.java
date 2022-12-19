package com.chuhsi.take.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.chuhsi.take.common.BaseContext;
import com.chuhsi.take.common.R;
import com.chuhsi.take.entity.Orders;
import com.chuhsi.take.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {

        //分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        //条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        orderService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 提交订单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单数据：{}", orders);

        orderService.submit(orders);

        return R.success("下单成功");
    }


    /**
     * 移动端订单列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> list(int page, int pageSize) {
        String id = BaseContext.getCurrentId().toString();
        //分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, id);
        queryWrapper.orderByAsc(Orders::getCheckoutTime);
        orderService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 修改订单状态
     *
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Orders orders) {
        log.info("订单信息：{}", orders);

        orderService.updateById(orders);

        return R.success("修改成功");
    }
}

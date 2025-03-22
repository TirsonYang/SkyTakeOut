package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单管理")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @ApiOperation("订单搜索")
    @RequestMapping(value = "/conditionSearch",method = RequestMethod.GET)
    public Result<PageResult> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("订单搜索:{}",ordersPageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 各个状态的订单数量统计
     * @return
     */
    @ApiOperation("各个状态的订单数量统计")
    @RequestMapping(value = "/statistics",method = RequestMethod.GET)
    public Result<OrderStatisticsVO> statistics(){
        log.info("各个状态的订单数量统计");
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @ApiOperation("查询订单详情")
    @RequestMapping(value = "/details/{id}",method = RequestMethod.GET)
    public Result<OrderVO> orderDetails(@PathVariable Long id){
        log.info("查询id为：{}的订单详情",id);
        OrderVO orderVO=orderService.detailsQuery(id);
        return Result.success(orderVO);
    }

    /**
     * 商家接单
     * @param ordersConfirmDTO
     * @return
     */
    @ApiOperation("商家接单")
    @RequestMapping(value = "/confirm",method = RequestMethod.PUT)
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        log.info("接单：{}",ordersConfirmDTO);
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 商家拒单
     * @param ordersRejectionDTO
     * @return
     * @throws Exception
     */
    @ApiOperation("商家拒单")
    @RequestMapping(value="/rejection",method = RequestMethod.PUT)
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        log.info("拒单：{}",ordersRejectionDTO);
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    @ApiOperation("取消订单")
    @PutMapping("/cancel")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        log.info("取消订单：{}",ordersCancelDTO);
        orderService.adminCancelOrders(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 商家派送订单
     * @param id
     * @return
     */
    @ApiOperation("商家派送订单")
    @RequestMapping(value = "/delivery/{id}",method = RequestMethod.PUT)
    public Result delivery(@PathVariable Long id){
        log.info("商家派送id为：{}的订单",id);
        orderService.delivery(id);
        return Result.success();
    }

    @ApiOperation("商家完成订单")
    @RequestMapping(value = "/complete/{id}",method = RequestMethod.PUT)
    public Result complete(@PathVariable Long id){
        log.info("商家完成id为：{}的订单",id);
        orderService.complete(id);
        return Result.success();
    }
}

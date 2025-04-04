package com.sky.controller.user;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "用户端订单相关接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @RequestMapping(value = "/submit",method = RequestMethod.POST)
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单，参数为：{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 历史订单分页查询
     * @param page,pageSize,status
     * @return
     */
    @ApiOperation("查询历史订单")
    @RequestMapping(value = "/historyOrders",method=RequestMethod.GET)
    public Result<PageResult> historyOrdersPageQuery(int page, int pageSize, Integer status){
        log.info("历史订单查询,{},{}",page,pageSize);
        PageResult pageResult = orderService.pageQuery(page,pageSize,status);
        return Result.success(pageResult);
    }

    @ApiOperation("订单详情")
    @RequestMapping(value = "/orderDetail/{id}",method=RequestMethod.GET)
    public Result<OrderVO> ordersDetails(@PathVariable Long id){
        log.info("查询id为：{}的订单详情",id);
        OrderVO orderVO = orderService.detailsQuery(id);
        return Result.success(orderVO);
    }


    /**
     * 取消订单
     * @param id
     * @return
     */
    @ApiOperation("取消订单")
    @RequestMapping(value = "/cancel/{id}",method=RequestMethod.PUT)
    public Result cancelOrders(@PathVariable Long id) throws Exception {
        log.info("将要取消id为：{}的订单",id);
        orderService.cancelOrders(id);
        return Result.success();
    }

    @ApiOperation("再来一单")
    @RequestMapping(value="/repetition/{id}",method = RequestMethod.POST)
    public Result repetition(@PathVariable Long id){
        log.info("将id为：{}的订单再来一单",id);
        orderService.repetition(id);
        return Result.success();
    }

    /**
     * 用户催单
     * @return
     */
    @ApiOperation("用户催单")
    @RequestMapping(value = "/reminder/{id}",method = RequestMethod.GET)
    public Result reminder(@PathVariable("id") Long id){
        log.info("客户催单");
        orderService.reminder(id);
        return Result.success();
    }

}

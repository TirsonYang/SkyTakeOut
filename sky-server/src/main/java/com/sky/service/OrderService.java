package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);


    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 历史订单分页查询
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    PageResult pageQuery(int pageNum,int pageSize,Integer status);

    /**
     * 订单详情
     * @param id
     * @return
     */
    OrderVO detailsQuery(Long id);

    /**
     * 取消订单
     * @param id
     */
    void cancelOrders(Long id) throws Exception;

    void repetition(Long id);
}

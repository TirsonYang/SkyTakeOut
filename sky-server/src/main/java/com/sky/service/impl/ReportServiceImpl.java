package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {


    private final OrderMapper orderMapper;

    public ReportServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 统计指定时间内营业额数据
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatics(LocalDate begin, LocalDate end) {
        //当前集合用于存放从begin到end之间的每天的日期

        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)){
            //计算指定十日起的后一天对应的日期
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        String dateListStr = StringUtils.join(dateList, ",");


        List<Double> turnoverList=new ArrayList<>();
        for (LocalDate date : dateList) {
            //查询date日期对应的营业额数据。营业额是指：状态为“已完成”的订单金额合计

            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map=new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);

            Double turnover=orderMapper.sumByMap(map);

            if (turnover==null){
                turnover=0.0;
            }

            turnoverList.add(turnover);
        }

        String turnoverListStr = StringUtils.join(turnoverList, ",");

        TurnoverReportVO turnoverReportVO=new TurnoverReportVO();
        turnoverReportVO.setTurnoverList(turnoverListStr);
        turnoverReportVO.setDateList(dateListStr);


        return turnoverReportVO;
    }
}

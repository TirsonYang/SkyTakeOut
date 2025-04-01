package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

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

    /**
     * 统计指定时间内用户数据
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }

        String dateListStr = StringUtils.join(dateList, ",");

        List<Integer> newUserList=new ArrayList<>();      //新增用户数
        List<Integer> totalUserList=new ArrayList<>();    //总用户数

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map=new HashMap<>();
            map.put("end",endTime);

            //总用户数量
            Integer total = userMapper.countByMap(map);

            map.put("begin",beginTime);

            Integer newUser = userMapper.countByMap(map);

            totalUserList.add(total);
            newUserList.add(newUser);

        }

        String totalUserListStr = StringUtils.join(totalUserList, ",");
        String newUserListStr = StringUtils.join(newUserList, ",");

        UserReportVO userReportVO=new UserReportVO();

        userReportVO.setDateList(dateListStr);
        userReportVO.setNewUserList(newUserListStr);
        userReportVO.setTotalUserList(totalUserListStr);

        return userReportVO;
    }
}

package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


public interface ReportService {

    /**
     * 统计指定时间内营业额数据
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverStatics(LocalDate begin, LocalDate end);

    /**
     * 统计指定时间内用户数据
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatics(LocalDate begin, LocalDate end);


    /**
     * 统计指定时间内订单数据
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderStatics(LocalDate begin, LocalDate end);

    /**
     * 统计指定时间内销量top10
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);
}

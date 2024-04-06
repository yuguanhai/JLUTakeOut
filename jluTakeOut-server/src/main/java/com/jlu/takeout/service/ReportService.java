package com.jlu.takeout.service;

import com.jlu.takeout.vo.OrderReportVO;
import com.jlu.takeout.vo.SalesTop10ReportVO;
import com.jlu.takeout.vo.TurnoverReportVO;
import com.jlu.takeout.vo.UserReportVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
public interface ReportService {

    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

    UserReportVO userStatistics(LocalDate begin, LocalDate end);

    OrderReportVO orderStatistics(LocalDate begin, LocalDate end);

    SalesTop10ReportVO orderTop10(LocalDate begin, LocalDate end);
}

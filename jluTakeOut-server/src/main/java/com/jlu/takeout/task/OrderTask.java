package com.jlu.takeout.task;

import com.jlu.takeout.entity.Orders;
import com.jlu.takeout.mapper.OrderMapper;
import com.jlu.takeout.service.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RecommendService recommendService;
    /**
     * 处理 超时订单方法
     */
    @Scheduled(cron = "0 * * * * ? ")
    public void processTimeOutOrder() {
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(15);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }

        }
    }

    /**
     * 处理一直在派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void processDeleveryOrder(){
        log.info("每天上午一点处理未完成订单：{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }

        }
    }

    @Scheduled(cron = "0 * * * * ?")
    public void updateDishSimilarities() {
        log.info("开始更新菜品相似度矩阵: {}", LocalDateTime.now());
        recommendService.generateAndUpdateDishSimilarities();
        log.info("菜品相似度矩阵更新完成: {}", LocalDateTime.now());
    }
    @Scheduled(cron = "0 * * * * ?")
    public void updateRecommendDishes() {
        log.info("开始更新推荐菜品: {}", LocalDateTime.now());
        recommendService.recommendDishes(5L);
        log.info("推荐菜品更新完成: {}", LocalDateTime.now());
    }
}

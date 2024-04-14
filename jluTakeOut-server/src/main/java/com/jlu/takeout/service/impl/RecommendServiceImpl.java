package com.jlu.takeout.service.impl;

import com.jlu.takeout.entity.DishSimilarity;
import com.jlu.takeout.entity.Relate;
import com.jlu.takeout.mapper.DishMapper;
import com.jlu.takeout.mapper.RecommendMapper;
import com.jlu.takeout.service.RecommendService;
import com.jlu.takeout.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private RecommendMapper recommendMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 计算皮尔森系数
     * @param ratingsForDishA
     * @param ratingsForDishB
     * @return
     */
    public double calculatePearsonCorrelation(Map<Long, Long> ratingsForDishA, Map<Long, Long> ratingsForDishB) {
        Set<Long> commonUsers = ratingsForDishA.keySet();
        commonUsers.retainAll(ratingsForDishB.keySet());

        int n = commonUsers.size();
        if (n == 0) return 0;

        double sum1 = 0.0, sum2 = 0.0, sum1Sq = 0.0, sum2Sq = 0.0, pSum = 0.0;
        for (Long user : commonUsers) {
            double ratingA = ratingsForDishA.get(user);
            double ratingB = ratingsForDishB.get(user);

            sum1 += ratingA;
            sum2 += ratingB;
            sum1Sq += ratingA * ratingA;
            sum2Sq += ratingB * ratingB;
            pSum += ratingA * ratingB;
        }

        double num = pSum - (sum1 * sum2 / n);
        double den = Math.sqrt((sum1Sq - sum1 * sum1 / n) * (sum2Sq - sum2 * sum2 / n));

        if (den == 0) return 0;
        return num / den;
    }

    /**
     * 对所有菜品生成相似度矩阵
     */
    @Transactional
    public void generateAndUpdateDishSimilarities() {
        // 1. 从数据库中获取所有评分数据
        List<Relate> allRatings = recommendMapper.getAllRelate();

        // 2. 将评分数据转换为菜品ID到用户评分映射的映射
        Map<Long, Map<Long, Long>> dishRatingsMap = allRatings.stream()
                .collect(Collectors.groupingBy(Relate::getDishId,
                        Collectors.toMap(Relate::getUserId, Relate::getGrade)));

        // 3. 对每对菜品计算皮尔森相关系数
        List<Long> dishIds = new ArrayList<>(dishRatingsMap.keySet());
        recommendMapper.deleteAllSimilarity();
        for (int i = 0; i < dishIds.size(); i++) {
            for (int j = i + 1; j < dishIds.size(); j++) {
                Long dishIdA = dishIds.get(i);
                Long dishIdB = dishIds.get(j);
                Map<Long, Long> ratingsA = dishRatingsMap.get(dishIdA);
                Map<Long, Long> ratingsB = dishRatingsMap.get(dishIdB);

                double similarity = calculatePearsonCorrelation(ratingsA, ratingsB);

                // 4. 将相似度结果保存回数据库
                DishSimilarity dishSimilarity = new DishSimilarity();
                dishSimilarity.setLeftDishId(dishIdA);
                dishSimilarity.setRightDishId(dishIdB);
                dishSimilarity.setSimilarity(similarity);

                recommendMapper.insertSimilarity(dishSimilarity); // 假设这个方法处理了插入或更新逻辑
            }
        }
    }


    public List<Long> recommendDishes(Long userId) {
        // 获取用户评分过的所有菜品
        List<Relate> userRatings = recommendMapper.getRelateByUserId(userId);

        // 获取所有的菜品相似度
        List<DishSimilarity> allSimilarities = recommendMapper.getAllSimilarity();

        // 构建每个菜品的相似菜品列表
        Map<Long, List<DishSimilarity>> similarityMap = allSimilarities.stream()
                .flatMap(ds -> Stream.of(
                        new AbstractMap.SimpleEntry<>(ds.getLeftDishId(), ds),
                        new AbstractMap.SimpleEntry<>(ds.getRightDishId(), ds)
                ))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        // 为用户推荐菜品
        Map<Long, Double> recommendationScores = new HashMap<>();
        userRatings.forEach(relate -> {
            Long dishId = relate.getDishId();
            List<DishSimilarity> similarities = similarityMap.get(dishId);
            if (similarities != null) {
                similarities.forEach(similarity -> {
                    Long similarDishId = similarity.getLeftDishId().equals(dishId) ? similarity.getRightDishId() : similarity.getLeftDishId();
                    double score = similarity.getSimilarity() * relate.getGrade();
                    recommendationScores.merge(similarDishId, score, Double::sum);
                });
            }
        });

        // 选择得分最高的5个菜品推荐给用户
        List<Long> collect = recommendationScores.entrySet().stream()
                // 过滤得分大于0的条目
                .filter(entry -> entry.getValue() > 0)
                // 根据得分进行降序排序
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                // 限制最大条目数量为5
                .limit(5)
                // 提取菜品ID
                .map(Map.Entry::getKey)
                // 收集结果到List
                .collect(Collectors.toList());
        //冷启动情况-没有用户喜欢的菜品
        if(collect.isEmpty()){
            collect.addAll(recommendMapper.findTopDishesInRelate());
        }
        return collect;
    }


}

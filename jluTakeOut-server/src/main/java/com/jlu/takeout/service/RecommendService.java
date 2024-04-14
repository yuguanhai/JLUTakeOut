package com.jlu.takeout.service;

import com.jlu.takeout.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RecommendService {
    void generateAndUpdateDishSimilarities();
    double calculatePearsonCorrelation(Map<Long, Long> ratingsForDishA, Map<Long, Long> ratingsForDishB);
    List<Long> recommendDishes(Long userId);
}

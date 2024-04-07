package com.jlu.takeout.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishSimilarity {
    // 条目id
    Long id;

    //菜品1id
    Long leftDishId;

    //菜品2id
    Long rightDishId;

    //相似度
    Double similarity;
}

package com.jlu.takeout.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * 用户-菜品-打分
 */
public class Relate {
    //条目id
    private Long id;

    //用户id
    private Long userId;

    //菜品id
    private Long dishId;

    //打分
    private Long grade;
}

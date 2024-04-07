package com.jlu.takeout.mapper;

import com.jlu.takeout.entity.DishSimilarity;
import com.jlu.takeout.entity.Relate;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Mapper
public interface RecommendMapper {

    @Select("select * from relate")
    List<Relate> getAllRelate();

    @Select("select * from relate where user_id = #{userId} and dish_id = #{dishId}")
    Relate getRelate(Long userId, Long dishId);

    @Insert("insert into relate(user_id, dish_id, grade) values (#{userId}, #{dishId}, #{grade})")
    void insertRelate(Relate relate);

    @Update("update relate set grade = #{grade} where user_id = #{userId} and dish_id = #{dishId}")
    void updateRelate(Relate relate);

    @Delete("delete from relate where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    @Select("select * from dish_similarity where left_dish_id = #{leftDishId} and right_dish_id = #{rightDishId}")
    void getSimilarity(Long userId);

    @Select("select * from dish_similarity")
    List<DishSimilarity> getAllSimilarity();

    @Insert("insert into dish_similarity(left_dish_id, right_dish_id, similarity) values (#{leftDishId}, #{rightDishId}, #{similarity})")
    DishSimilarity insertSimilarity(DishSimilarity dishSimilarity);

    @Update("update dish_similarity set similarity = #{similarity} where left_dish_id = #{leftDishId} and right_dish_id = #{rightDishId}")
    void updateSimilarity(DishSimilarity dishSimilarity);

    @Delete("delete from dish_similarity where left_dish_id = #{leftDishId} and right_dish_id = #{rightDishId}")
    void deleteSimilarity(DishSimilarity dishSimilarity);

}

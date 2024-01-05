package com.sky.mapper;

import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    void insertBatch(List<DishFlavor> dishFlavors);

    /**
     * 通过菜品id删除口味
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    /**
     * 通过多个菜品id删除口味
     * @param ids
     */
    void deleteByDishIds(List<Long> ids);

    /**
     * 根据菜品id查询口味数据
     * @param dishId
     */
    @Select("select * from dish_flavor where dish_id=#{dish_id}")
    List<DishFlavor> getByDishId(Long dishId);
}

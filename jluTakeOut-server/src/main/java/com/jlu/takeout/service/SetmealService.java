package com.jlu.takeout.service;

import com.jlu.takeout.dto.SetmealDTO;
import com.jlu.takeout.dto.SetmealPageQueryDTO;
import com.jlu.takeout.entity.Setmeal;
import com.jlu.takeout.vo.DishItemVO;
import com.jlu.takeout.vo.SetmealVO;
import com.jlu.takeout.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SetmealService {
    void saveWithSetmealDishes(SetmealDTO setmealDTO);

    PageResult pageResult(SetmealPageQueryDTO setmealPageQueryDTO);

    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询套餐和关联的菜品数据
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 修改套餐
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}

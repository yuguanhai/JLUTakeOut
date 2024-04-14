package com.jlu.takeout.controller.user;

import com.jlu.takeout.constant.StatusConstant;
import com.jlu.takeout.context.BaseContext;
import com.jlu.takeout.entity.Dish;
import com.jlu.takeout.result.Result;
import com.jlu.takeout.service.DishService;
import com.jlu.takeout.service.RecommendService;
import com.jlu.takeout.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.events.Event;

import java.util.ArrayList;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        if(categoryId==1){
            List<Long>  dishIds= recommendService.recommendDishes(BaseContext.getCurrentId());
            List<DishVO> dishVOList = dishService.listWithFlavorByDishIds(dishIds);
            return Result.success(dishVOList);
        }

        //构造redis key 规则：dish_分类id
        String key = "dish_"+ categoryId;
        //先查redis
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if(list!=null&& !list.isEmpty()){
            //是在redis里面获取的
            //如果存在，返回redis里面的对象
            return Result.success(list);
        }
        //如果不存在，查数据库，并且放到redis里
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        //查询起售中的菜品
        list = dishService.listWithFlavor(dish);
        redisTemplate.opsForValue().set(key,list);
        return Result.success(list);
    }

}

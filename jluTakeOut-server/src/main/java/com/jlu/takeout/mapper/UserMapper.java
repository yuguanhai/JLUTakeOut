package com.jlu.takeout.mapper;

import com.jlu.takeout.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByUserId(String openid);

    /**
     * 插入数据
     * @param user
     */
    void insert(User user);
    @Select("select * from user where id = #{id}")
    User getById(Long userId);

}

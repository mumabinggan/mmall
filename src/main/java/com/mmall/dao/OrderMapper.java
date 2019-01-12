package com.mmall.dao;

import com.mmall.pojo.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByUserIdAndOrderNO(@Param("userId") Integer userId,
                                   @Param("orderNO") Long orderNO);

    Order selectByOrderNO(Long orderNO);
}
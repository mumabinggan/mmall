package com.mmall.dao;

import com.mmall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int updateByShipping(Shipping shipping);

    int deleteByUserIdAndId(@Param(value = "userId") Integer userId,
                            @Param(value = "id") Integer id);

    Shipping selectByUserIdAndId(@Param(value = "userId") Integer userId,
                                 @Param(value = "id") Integer id);

    List<Shipping> selectByUserId(Integer userId);
}
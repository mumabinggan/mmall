package com.mmall.dao;

import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUserIdAndProductId(@Param(value = "userId") Integer userId,
                                    @Param(value = "productId") Integer productId);

    List<Cart> selectByUserId(Integer userId);

    int checkoutAllCheckedByUserId(Integer userId);

    int deleteByUserIdAndProductIdList(@Param(value = "userId") Integer userId,
                                       @Param(value = "productIdList") List<String> productIdList);

    int updateCheckedByUserIdAndProductIdAndChecked(@Param(value = "userId") Integer userId,
                                                    @Param(value = "productId") Integer productId,
                                                    @Param(value = "checked") Integer checked);

    int selectProductCountFromCartByUserId(Integer userId);
}
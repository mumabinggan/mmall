package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkoutByUsername(String username);

    int checkoutByEmail(String email);

    User selectByUsername(String username);

    User selectByUsernameAndPassword(@Param("username") String username,
                                     @Param("password") String password);

    String selectQuestionByUsername(String username);

    int checkoutByUsernameAndQuestionAndAnswer(@Param("username") String username,
                                               @Param("question") String question,
                                               @Param("answer") String answer);

    int updateByUsernameAndPassword(@Param("username") String username,
                                    @Param("password") String password);

    int checkoutByUserIdAndOldPassword(@Param("userId") Integer userId,
                                       @Param("password") String password);

    int checkoutEmailByDiffUserId(@Param("userId") Integer userId,
                                  @Param("email") String email);
}
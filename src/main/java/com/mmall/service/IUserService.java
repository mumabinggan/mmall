package com.mmall.service;

import com.mmall.common.JHResponse;
import com.mmall.pojo.User;

public interface IUserService {

	JHResponse<User> login(String username, String password);

	JHResponse<User> register(User user);

	JHResponse<String> getQuestion(String username);

	JHResponse<String> checkoutByUsernameAndQuestionAndAnswer(String username, String question, String answer);

	JHResponse<String> resetPassword(String username, String passwordNew, String token);

	JHResponse<String> resetPassword(User user, String passwordOld, String passwordNew);

	JHResponse<User> updateUserInfo(User user, User updateUser);

	JHResponse<User> getInfomation(Integer userId);

	JHResponse checkAdminRole(User user);
}

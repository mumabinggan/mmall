package com.mmall.service.impl;

import com.mmall.common.*;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.utils.JHStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public JHResponse<User> login(String username, String password) {
		int count = userMapper.checkoutByUsername(username);
		if (count == 0) {
			return JHResponse.createByError(JHResponseCode.Error_UsernameUnExist);
		}
		User user = userMapper.selectByUsernameAndPassword(username, password);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.Error_PasswordError);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_LoginSuccess, user);
	}

	@Override
	public JHResponse<User> register(User user) {
		//check username
		String username = user.getUsername();
		if (JHStringUtils.isBlank(username)) {
			return JHResponse.createByError(JHResponseCode.Error_UsernameIsEmpty);
		}
		int count = userMapper.checkoutByUsername(username);
		if (count > 0) {
			return JHResponse.createByError(JHResponseCode.Error_UsernameHasExist);
		}

		//check email
		String email = user.getEmail();
		if (JHStringUtils.isBlank(email)) {
			return JHResponse.createByError(JHResponseCode.Error_EmailIsEmpty);
		}
		count = userMapper.checkoutByEmail(email);
		if (count > 0) {
			return JHResponse.createByError(JHResponseCode.Error_EmailHasExist);
		}

		user.setRole(JHConst.Role.Custom);

		int result = userMapper.insert(user);
		if (result > 0) {
			return JHResponse.createBySuccess(JHResponseCode.Success_RegisterSuccess);
		}
		return JHResponse.createBySuccess(JHResponseCode.Error_RegisterError);
	}

	@Override
	public JHResponse<String> getQuestion(String username) {
		//check username
		if (JHStringUtils.isBlank(username)) {
			return JHResponse.createByError(JHResponseCode.Error_UsernameIsEmpty);
		}
		int count = userMapper.checkoutByUsername(username);
		if (count > 0) {
			String question = userMapper.selectQuestionByUsername(username);
			if (JHStringUtils.isNotBlank(question)) {
				return JHResponse.createBySuccess(JHResponseCode.Success_GetQuestionSuccess);
			}
		}
		return JHResponse.createByError(JHResponseCode.InvalidArgument);
	}

	@Override
	public JHResponse<String> checkoutByUsernameAndQuestionAndAnswer(String username, String question, String answer) {
		//check username
		if (JHStringUtils.isBlank(username)) {
			return JHResponse.createByError(JHResponseCode.Error_UsernameIsEmpty);
		}
		int count = userMapper.checkoutByUsername(username);
		if (count > 0) {
			count = userMapper.checkoutByUsernameAndQuestionAndAnswer(username, question, answer);
			if (count > 0) {
				String forgetToken = UUID.randomUUID().toString();
				JHTokenCache.setKey(username, forgetToken);
				return JHResponse.createBySuccess(JHResponseCode.Success_CheckQuestionAndAnswerSuccess, forgetToken);
			}
		}
		return JHResponse.createBySuccess(JHResponseCode.InvalidArgument);
	}

	@Override
	public JHResponse resetPassword(String username, String passwordNew, String token) {
		//check username
		if (JHStringUtils.isBlank(username)) {
			return JHResponse.createByError(JHResponseCode.Error_UsernameIsEmpty);
		}
		int count = userMapper.checkoutByUsername(username);
		if (count <= 0) {
			return JHResponse.createByError(JHResponseCode.Error_UsernameUnExist);
		}

		//check token
		if (JHStringUtils.isBlank(token)) {
			return JHResponse.createByError(JHResponseCode.Error_TokenIsEmpty);
		}
		if (!JHStringUtils.equals(JHTokenCache.getValue(username), token)) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}

		//check passwordNew
		if (JHStringUtils.isBlank(passwordNew)) {
			return JHResponse.createByError(JHResponseCode.Error_NewPasswordIsEmpty);
		}
		else {
			count = userMapper.updateByUsernameAndPassword(username, passwordNew);
			if (count > 0) {
				return JHResponse.createBySuccess(JHResponseCode.Success_UpdateNewPasswordSuccess);
			}
			else {
				return JHResponse.createByError(JHResponseCode.Error_UpdateNewPasswordError);
			}
		}
	}

	@Override
	public JHResponse<String> resetPassword(User user, String passwordOld, String passwordNew) {
		int count = userMapper.checkoutByUserIdAndOldPassword(user.getId(), passwordOld);
		if (count <= 0) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		count = userMapper.updateByUsernameAndPassword(user.getUsername(), passwordNew);
		if (count > 0) {
			return JHResponse.createBySuccess(JHResponseCode.Success_UpdateNewPasswordSuccess);
		} else {
			return JHResponse.createByError(JHResponseCode.Error_UpdateNewPasswordError);
		}
	}

	@Override
	public JHResponse<User> updateUserInfo(User user, User updateUser) {
		if (updateUser == null) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		if (user.getId() != updateUser.getId()) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		if (!user.getUsername().equals(updateUser.getUsername())) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		int count = userMapper.checkoutEmailByDiffUserId(user.getId(), updateUser.getEmail());
		if (count > 0) {
			return JHResponse.createByError(JHResponseCode.Error_EmailUnExist);
		}
		else {
			updateUser.setRole(JHConst.Role.Custom);
			count = userMapper.insertSelective(updateUser);
			if (count > 0) {
				return JHResponse.createBySuccess(JHResponseCode.Success_UpdateUserInfoSuccess, updateUser);
			}
			else {
				return JHResponse.createByError(JHResponseCode.Error_UpdateUserInfoError);
			}
		}
	}

	@Override
	public JHResponse<User> getInfomation(Integer userId) {
		User user = userMapper.selectByPrimaryKey(userId);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.Error_GetUserInfoError);
		}
		user.setPassword(null);
		return JHResponse.createBySuccess(JHResponseCode.Success_GetUserInfoSuccess);
	}

	@Override
	public JHResponse checkAdminRole(User user) {
		if (user != null && user.getRole() == JHConst.Role.Admin) {
			return JHResponse.createBySuccess(JHResponseCode.Success);
		}
		return JHResponse.createBySuccess(JHResponseCode.Error);
	}
}

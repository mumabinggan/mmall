package com.mmall.controller.user;

import com.mmall.common.JHConst;
import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

	@Autowired
	private IUserService iUserService;

	/**
	 * 用户名密码登录
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "login.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<User> login(String username,
								  String password,
								  HttpSession session,
								  @RequestHeader HttpHeaders headers) {
		JHResponse<User> response = iUserService.login(username, password);
		if (response.isSuccess()) {
			session.setAttribute(JHConst.SessionUserKey, response.getData());
			return response;
		}
		return response;
	}

	/**
	 * 注册
	 * @param user
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "register.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<User> register(User user, HttpSession session) {
		JHResponse<User> response = iUserService.register(user);
		if (response.isSuccess()) {
			session.setAttribute(JHConst.SessionUserKey, response.getData());
			return response;
		}
		return response;
	}

	/**
	 * 注销登录
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "logout.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse logout(HttpSession session) {
		session.removeAttribute(JHConst.SessionUserKey);
		return JHResponse.createBySuccess();
	}

	/**
	 * 得到登录用户信息
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "getUserInfo.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<User> getUserInfo(HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		return JHResponse.createBySuccess(JHResponseCode.Success_GetUserInfoSuccess, user);
	}

	/**
	 * 得到用户提问的问题
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "forget_get_Question.do", method = RequestMethod.GET)
	@ResponseBody
	JHResponse<String> getQuestion(String username) {
		return iUserService.getQuestion(username);
	}

	/**
	 * 核对问题及答案并返回token
	 * @param username
	 * @param question
	 * @param answer
	 * @return
	 */
	@RequestMapping(value = "forget_check_answer.do", method = RequestMethod.GET)
	@ResponseBody
	JHResponse<String> checkQuestionAndAnswer(String username, String question, String answer) {
		return iUserService.checkoutByUsernameAndQuestionAndAnswer(username, question, answer);
	}

	/**
	 * 忘记密码重置密码
	 * @param username
	 * @param password
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "forget_reset_password.do", method = RequestMethod.GET)
	@ResponseBody
	JHResponse<String> forgetResetPassword(String username, String password, String token) {
		return iUserService.resetPassword(username, password, token);
	}

	/**
	 * 修改密码(用户登录的时候)
	 * @param session
	 * @param passwordOld
	 * @param passwordNew
	 * @return
	 */
	@RequestMapping(value = "reset_password.do", method = RequestMethod.GET)
	@ResponseBody
	JHResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		else {
			return iUserService.resetPassword(user, passwordOld, passwordNew);
		}
	}

	/**
	 * 更新用户信息
	 * @param session
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "updateUserInfo.do", method = RequestMethod.GET)
	@ResponseBody
	JHResponse<User> updateUserInfo(HttpSession session, User user) {
		User currentUser = (User) session.getAttribute(JHConst.SessionUserKey);
		if (currentUser == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		else {
			return iUserService.updateUserInfo(currentUser, user);
		}
	}

	/**
	 * 得到用户详情信息
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "getUserDetailInfomation.do", method = RequestMethod.GET)
	@ResponseBody
	JHResponse<User> getUserDetailInfomation(HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		else {
			return iUserService.getInfomation(user.getId());
		}
	}
}

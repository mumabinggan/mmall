package com.mmall.controller.manager;

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
@RequestMapping("/admin/")
public class AdminUserController {

	@Autowired
	private IUserService iUserService;

	/**
	 * 管理员登录
	 * @param username
	 * @param password
	 * @param session
	 * @param headers
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
			User user = response.getData();
			user.setPassword(null);
			if (user.getRole() == JHConst.Role.Custom) {
				response = JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
			} else {
				session.setAttribute(JHConst.SessionUserKey, user);
			}
		}
		return response;
	}
}

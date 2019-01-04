package com.mmall.controller.manager;

import com.mmall.common.JHConst;
import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.util.resources.cldr.to.CalendarData_to_TO;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/")
public class AdminCategoryController {

	@Autowired
	private ICategoryService iCategoryService;

	@Autowired
	private IUserService iUserService;

	/**
	 * 根据父categoryId得到子categoryList(第一级子分类)
	 * @param categoryId
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "getSubCategoryList.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<List<Category>> getSubCategoryList(Integer categoryId, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iCategoryService.getSubCategoryListByCategoryId(categoryId);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 增加分类
	 * @param categoryName
	 * @param parentId
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "addCategory.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse addCategory(String categoryName, Integer parentId, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iCategoryService.addCategory(categoryName, parentId);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 更新分类
	 * @param category
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "updateCategory.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse updateCategory(Category category, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iCategoryService.updateCategory(category);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 根据分类id得到所有分类(递归)
	 * @param categoryId
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "getDeepSubCategoryListByCategoryId.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse getDeepSubCategoryListByCategoryId(Integer categoryId, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iCategoryService.getDeepSubCategoryListByCategoryId(categoryId);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}
}

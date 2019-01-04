package com.mmall.service.impl;

import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.utils.JHStringUtils;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public JHResponse<List<Category>> getSubCategoryListByCategoryId(Integer categoryId) {
		List<Category> list = categoryMapper.getSubCategoryListByCategoryId(categoryId);
		return JHResponse.createBySuccess(JHResponseCode.Success_GetSubCategoryListSuccess, list);
	}

	@Override
	public JHResponse addCategory(String categoryName, Integer parentCategoryId) {
		if (parentCategoryId == null || JHStringUtils.isBlank(categoryName)) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		Category category = new Category(parentCategoryId, categoryName, true);

		int rowCount = categoryMapper.insert(category);
		if (rowCount > 0) {
			return JHResponse.createBySuccess(JHResponseCode.Success_AddCategorySuccess);
		} else {
			return JHResponse.createByError(JHResponseCode.Error_AddCategoryError);
		}
	}

	@Override
	public JHResponse updateCategory(Category category) {
		if (category.getId() == null || JHStringUtils.isBlank(category.getName())) {
			return JHResponse.createByError(JHResponseCode.InvalidArgument);
		}
		int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
		if (rowCount > 0) {
			return JHResponse.createBySuccess(JHResponseCode.Success_UpdateCategorySuccess);
		} else {
			return JHResponse.createByError(JHResponseCode.Error_UpdateCategoryError);
		}
	}

	@Override
	public JHResponse<Set<Category>> getDeepSubCategoryListByCategoryId(Integer categoryId) {
		Set categorySet = new HashSet();
		this.getDeepSubCategorySet(categoryId, categorySet);
		return JHResponse.createBySuccess(JHResponseCode.Success_GetDeepSubCategorySuccess, categorySet);
	}

	public void getDeepSubCategorySet(Integer categoryId, Set<Category> categorySet) {
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category != null) {
			categorySet.add(category);
		}
		List<Category> list = this.getSubCategoryListByCategoryId(categoryId).getData();
		for (Category item : list) {
			categorySet.add(item);
			this.getDeepSubCategorySet(item.getId(), categorySet);
		}
	}
}

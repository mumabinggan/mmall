package com.mmall.service;

import com.mmall.common.JHResponse;
import com.mmall.pojo.Category;

import java.util.List;
import java.util.Set;

public interface ICategoryService {

	JHResponse<List<Category>> getSubCategoryListByCategoryId(Integer categoryId);

	JHResponse addCategory(String categoryName, Integer parentCategoryId);

	JHResponse updateCategory(Category category);

	JHResponse<Set<Category>> getDeepSubCategoryListByCategoryId(Integer categoryId);
}

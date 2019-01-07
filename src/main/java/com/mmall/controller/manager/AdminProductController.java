package com.mmall.controller.manager;


import com.github.pagehelper.PageInfo;
import com.mmall.common.JHConst;
import com.mmall.common.JHResponse;
import com.mmall.common.JHResponseCode;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileUploadService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.vo.FileUploadVO;
import com.mmall.vo.RichTextImgFileUplaodVO;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

	@Autowired
	private IUserService iUserService;

	@Autowired
	private IProductService iProductService;

	@Autowired
	private IFileUploadService iFileUploadService;

	/**
	 * 发布商品
	 * @param product
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "productSave.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse productSave(Product product, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iProductService.saveOrUpdateProduct(product);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 设置商品状态
	 * @param productId
	 * @param status
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "setSaleStatus.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse setSaleStatus(Integer productId, Integer status, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iProductService.setSaleStatus(productId, status);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 得到商品详情
	 * @param productId
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "getProductDetail.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse getProductDetail(Integer productId, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iProductService.getProductDetail(productId, true);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 得到商品列表
	 * @param pageNum
	 * @param pageSize
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "getProductList.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<PageInfo> getProductList(@RequestParam(value = "pageNum", defaultValue = "1")
														   Integer pageNum,
											   @RequestParam(value = "pageSize", defaultValue = "20")
													   Integer pageSize,
											   HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iProductService.getProductList(pageNum, pageSize);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 搜索商品列表
	 * @param productName
	 * @param productId
	 * @param pageNum
	 * @param pageSize
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "searchProductList.do", method = RequestMethod.GET)
	@ResponseBody
	public JHResponse<PageInfo> searchProductListByProductNameAndProductId(String productName,
																		   Integer productId,
																		   @RequestParam(value = "pageNum", defaultValue = "1")
																				   Integer pageNum,
																		   @RequestParam(value = "pageSize", defaultValue = "20")
																				   Integer pageSize,
																		   HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iProductService.searchProductListByProductNameAndProductId(productName,
					productId, pageNum, pageSize);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 上传文件
	 * @param file
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "uploadFiles.do", method = RequestMethod.POST)
	@ResponseBody
	public JHResponse<FileUploadVO> uploadFiles(MultipartFile file, HttpSession session) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return JHResponse.createByError(JHResponseCode.ReLogin);
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			String path = session.getServletContext().getRealPath("upload");
			return iProductService.uploadFiles(file, path);
		}
		else {
			return JHResponse.createByError(JHResponseCode.Error_UnAdminLogin);
		}
	}

	/**
	 * 上传富文本图片文件(基于Simditor)
	 * @param file
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "uploadRichTextImgFiles.do", method = RequestMethod.POST)
	@ResponseBody
	public RichTextImgFileUplaodVO uploadRichTextImgFiles(MultipartFile file, HttpSession session, HttpServletResponse servletResponse) {
		User user = (User) session.getAttribute(JHConst.SessionUserKey);
		if (user == null) {
			return new RichTextImgFileUplaodVO(false, JHResponseCode.Error_UserUnExist.getMsg());
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			String path = session.getServletContext().getRealPath("upload");
			JHResponse<FileUploadVO> response = iProductService.uploadFiles(file, path);
			if (response.isSuccess()) {
				servletResponse.addHeader("Access-Control-Allow-Headers", "X-File-Name");
				return new RichTextImgFileUplaodVO(true,
						JHResponseCode.Success_UploadFilesSuccess.getMsg(),
						response.getData().getUrl());
			} else {
				return new RichTextImgFileUplaodVO(false, JHResponseCode.Error_UploadFilesError.getMsg());
			}
		}
		else {
			return new RichTextImgFileUplaodVO(false, JHResponseCode.Error_UnAdminLogin.getMsg());
		}
	}
}

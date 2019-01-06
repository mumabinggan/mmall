package com.mmall.vo;

import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.utils.JHDateTimeUtil;
import com.mmall.utils.JHPropertiesUtil;

import java.math.BigDecimal;

public class ProductDetailVO {

	private Integer id;
	private Integer categoryId;
	private String name;
	private String subTitle;
	private String mainImage;
	private String subImages;
	private String detail;
	private BigDecimal price;
	private Integer stock;
	private Integer status;
	private String createTime;
	private String updateTime;

	private String imageHost;
	private Integer parentCategoryId;

	public ProductDetailVO(Product product, Category category) {
		this.id = product.getId();
		this.categoryId = category.getId();
		this.name = product.getName();
		this.subTitle = product.getSubtitle();
		this.mainImage = product.getMainImage();
		this.subImages = product.getSubImages();
		this.detail = product.getDetail();
		this.price = product.getPrice();
		this.stock = product.getStock();
		this.status = product.getStatus();
		this.createTime = JHDateTimeUtil.dateToStr(product.getCreateTime());
		this.updateTime = JHDateTimeUtil.dateToStr(product.getUpdateTime());
		this.parentCategoryId = category.getParentId();
		this.imageHost = JHPropertiesUtil.getProperty("ftp.server.http.prefix",
				"http://img.happymmall.com/");
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public String getSubImages() {
		return subImages;
	}

	public void setSubImages(String subImages) {
		this.subImages = subImages;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getImageHost() {
		return imageHost;
	}

	public void setImageHost(String imageHost) {
		this.imageHost = imageHost;
	}

	public Integer getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Integer parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}
}

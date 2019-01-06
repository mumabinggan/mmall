package com.mmall.vo;

import com.mmall.pojo.Product;
import com.mmall.utils.JHPropertiesUtil;

import java.math.BigDecimal;

public class ProductListItemVO {

	private Integer id;
	private Integer categoryId;

	private String name;
	private String subTitle;
	private String mainImage;
	private BigDecimal price;

	private Integer status;
	private String imageHost;

	public ProductListItemVO(Product product) {
		this.id = product.getId();
		this.categoryId = product.getCategoryId();
		this.name = product.getName();
		this.subTitle = product.getSubtitle();
		this.mainImage = product.getMainImage();
		this.price = product.getPrice();
		this.status = product.getStatus();
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getImageHost() {
		return imageHost;
	}

	public void setImageHost(String imageHost) {
		this.imageHost = imageHost;
	}
}

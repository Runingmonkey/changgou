package com.changgou.user.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/****
 * @Author:itheima
 * @Description:ItemsHistory构建
 *****/
@ApiModel(description = "ItemsHistory",value = "ItemsHistory")
@Table(name="items_history")
public class ItemsHistory implements Serializable{

	@ApiModelProperty(value = "",required = false)
	@Id
    @Column(name = "id")
	private String id;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "name")
	private String name;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "skuid")
	private String skuid;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "image")
	private String image;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "userid")
	private String userid;//



	//get方法
	public String getId() {
		return id;
	}

	//set方法
	public void setId(String id) {
		this.id = id;
	}
	//get方法
	public String getName() {
		return name;
	}

	//set方法
	public void setName(String name) {
		this.name = name;
	}
	//get方法
	public String getSkuid() {
		return skuid;
	}

	//set方法
	public void setSkuid(String skuid) {
		this.skuid = skuid;
	}
	//get方法
	public String getImage() {
		return image;
	}

	//set方法
	public void setImage(String image) {
		this.image = image;
	}
	//get方法
	public String getUserid() {
		return userid;
	}

	//set方法
	public void setUserid(String userid) {
		this.userid = userid;
	}


}

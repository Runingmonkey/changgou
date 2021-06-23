package com.changgou.user.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/****
 * @Author:itheima
 * @Description:Comments构建
 *****/
@ApiModel(description = "Comments",value = "Comments")
@Table(name="comments")
public class Comments implements Serializable{

	@ApiModelProperty(value = "",required = false)
	@Id
    @Column(name = "id")
	private String id;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "username")
	private String username;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "content")
	private String content;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "createtime")
	private Date createtime;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "images")
	private String images;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "count")
	private Integer count;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "level")
	private Integer level;//

	@ApiModelProperty(value = "",required = false)
    @Column(name = "score")
	private Integer score;//

	@ApiModelProperty(value = "类型，1：用户评价，2：客户回复",required = false)
    @Column(name = "type")
	private Integer type;//类型，1：用户评价，2：客户回复

	@ApiModelProperty(value = "客服回复的评价id",required = false)
    @Column(name = "pid")
	private String pid;//客服回复的评价id

	@ApiModelProperty(value = "",required = false)
    @Column(name = "spuid")
	private String spuid;//



	//get方法
	public String getId() {
		return id;
	}

	//set方法
	public void setId(String id) {
		this.id = id;
	}
	//get方法
	public String getUsername() {
		return username;
	}

	//set方法
	public void setUsername(String username) {
		this.username = username;
	}
	//get方法
	public String getContent() {
		return content;
	}

	//set方法
	public void setContent(String content) {
		this.content = content;
	}
	//get方法
	public Date getCreatetime() {
		return createtime;
	}

	//set方法
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	//get方法
	public String getImages() {
		return images;
	}

	//set方法
	public void setImages(String images) {
		this.images = images;
	}
	//get方法
	public Integer getCount() {
		return count;
	}

	//set方法
	public void setCount(Integer count) {
		this.count = count;
	}
	//get方法
	public Integer getLevel() {
		return level;
	}

	//set方法
	public void setLevel(Integer level) {
		this.level = level;
	}
	//get方法
	public Integer getScore() {
		return score;
	}

	//set方法
	public void setScore(Integer score) {
		this.score = score;
	}
	//get方法
	public Integer getType() {
		return type;
	}

	//set方法
	public void setType(Integer type) {
		this.type = type;
	}
	//get方法
	public String getPid() {
		return pid;
	}

	//set方法
	public void setPid(String pid) {
		this.pid = pid;
	}
	//get方法
	public String getSpuid() {
		return spuid;
	}

	//set方法
	public void setSpuid(String spuid) {
		this.spuid = spuid;
	}


}

package com.changgou.order.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/****
 * @Author:itheima
 * @Description:ReturnOrderItem构建
 *****/
@ApiModel(description = "ReturnOrderItem",value = "ReturnOrderItem")
@Table(name="tb_return_order_item")
public class ReturnOrderItem implements Serializable{

	@ApiModelProperty(value = "ID",required = false)
	@Id
    @Column(name = "id")
	private Long id;//ID

	@ApiModelProperty(value = "分类ID",required = false)
    @Column(name = "category_id")
	private Long categoryId;//分类ID

	@ApiModelProperty(value = "SPU_ID",required = false)
    @Column(name = "spu_id")
	private Long spuId;//SPU_ID

	@ApiModelProperty(value = "SKU_ID",required = false)
    @Column(name = "sku_id")
	private Long skuId;//SKU_ID

	@ApiModelProperty(value = "订单ID",required = false)
    @Column(name = "order_id")
	private Long orderId;//订单ID

	@ApiModelProperty(value = "订单明细ID",required = false)
    @Column(name = "order_item_id")
	private Long orderItemId;//订单明细ID

	@ApiModelProperty(value = "退货订单ID",required = false)
    @Column(name = "return_order_id")
	private Long returnOrderId;//退货订单ID

	@ApiModelProperty(value = "标题",required = false)
    @Column(name = "title")
	private String title;//标题

	@ApiModelProperty(value = "单价",required = false)
    @Column(name = "price")
	private Integer price;//单价

	@ApiModelProperty(value = "数量",required = false)
    @Column(name = "num")
	private Integer num;//数量

	@ApiModelProperty(value = "总金额",required = false)
    @Column(name = "money")
	private Integer money;//总金额

	@ApiModelProperty(value = "支付金额",required = false)
    @Column(name = "pay_money")
	private Integer payMoney;//支付金额

	@ApiModelProperty(value = "图片地址",required = false)
    @Column(name = "image")
	private String image;//图片地址

	@ApiModelProperty(value = "重量",required = false)
    @Column(name = "weight")
	private Integer weight;//重量



	//get方法
	public Long getId() {
		return id;
	}

	//set方法
	public void setId(Long id) {
		this.id = id;
	}
	//get方法
	public Long getCategoryId() {
		return categoryId;
	}

	//set方法
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	//get方法
	public Long getSpuId() {
		return spuId;
	}

	//set方法
	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}
	//get方法
	public Long getSkuId() {
		return skuId;
	}

	//set方法
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	//get方法
	public Long getOrderId() {
		return orderId;
	}

	//set方法
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	//get方法
	public Long getOrderItemId() {
		return orderItemId;
	}

	//set方法
	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}
	//get方法
	public Long getReturnOrderId() {
		return returnOrderId;
	}

	//set方法
	public void setReturnOrderId(Long returnOrderId) {
		this.returnOrderId = returnOrderId;
	}
	//get方法
	public String getTitle() {
		return title;
	}

	//set方法
	public void setTitle(String title) {
		this.title = title;
	}
	//get方法
	public Integer getPrice() {
		return price;
	}

	//set方法
	public void setPrice(Integer price) {
		this.price = price;
	}
	//get方法
	public Integer getNum() {
		return num;
	}

	//set方法
	public void setNum(Integer num) {
		this.num = num;
	}
	//get方法
	public Integer getMoney() {
		return money;
	}

	//set方法
	public void setMoney(Integer money) {
		this.money = money;
	}
	//get方法
	public Integer getPayMoney() {
		return payMoney;
	}

	//set方法
	public void setPayMoney(Integer payMoney) {
		this.payMoney = payMoney;
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
	public Integer getWeight() {
		return weight;
	}

	//set方法
	public void setWeight(Integer weight) {
		this.weight = weight;
	}


}

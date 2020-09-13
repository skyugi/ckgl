package com.lzh.wms.statistics.domain;

/**
 * 统计分析echarts基础数据格式
 * @author lzh
 * @date 2020-03-28
 */
public class BaseEntity {

	private String name;
	private Double value;
	public BaseEntity() {
	}

	public BaseEntity(String name, Double value) {
		super();
		this.name = name;
		this.value = value;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}


}

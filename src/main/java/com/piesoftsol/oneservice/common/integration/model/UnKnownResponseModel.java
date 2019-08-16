package com.piesoftsol.oneservice.common.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.sf.cglib.beans.BeanMap;

public class UnKnownResponseModel extends ServiceResponse{

	@JsonProperty(value = "result")
	BeanMap beanMap;

	public BeanMap getBeanMap() {
		return beanMap;
	}

	public void setBeanMap(BeanMap beanMap) {
		this.beanMap = beanMap;
	}
	
	
}

/*
 * 版权所有 ©2011-2016 格点软件(北京)有限公司 All rights reserved.
 * 
 * 未经书面授权，不得擅自复制、影印、储存或散播。
 */
package com.gridsofts.install4j.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author lei
 */
public class Project implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private List<IStep> stepList;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the stepList
	 */
	public List<IStep> getStepList() {
		return stepList;
	}

	/**
	 * @param stepList
	 *            the stepList to set
	 */
	public void setStepList(List<IStep> stepList) {
		this.stepList = stepList;
	}
}

/*
 * 版权所有 ©2011-2016 格点软件(北京)有限公司 All rights reserved.
 * 
 * 未经书面授权，不得擅自复制、影印、储存或散播。
 */
package com.gridsofts.install4j.adapter;

import javax.swing.JPanel;

import com.gridsofts.install4j.model.IStep;

/**
 * 文件复制
 * 
 * @author lei
 */
public class FileCopy implements IStep {
	private static final long serialVersionUID = 1L;
	
	private boolean isFirst, isLast;
	
	private String name;
	private String source;
	private String destination;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public JPanel getStepPane() {
		return null;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public boolean onPreviousStep() {
		return false;
	}

	public boolean onNextStep() {
		return false;
	}

	public boolean onFinish() {
		return false;
	}

}

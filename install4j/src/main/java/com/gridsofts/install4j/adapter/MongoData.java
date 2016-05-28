/*
 * 版权所有 ©2011-2016 格点软件(北京)有限公司 All rights reserved.
 * 
 * 未经书面授权，不得擅自复制、影印、储存或散播。
 */
package com.gridsofts.install4j.adapter;

import javax.swing.JPanel;

import com.gridsofts.install4j.model.IStep;

/**
 * MongoDB数据库初始化
 * 
 * @author lei
 */
public class MongoData implements IStep {
	private static final long serialVersionUID = 1L;
	
	private boolean isFirst, isLast;
	
	private String name;
	private String collection;
	private String jsonData;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the collection
	 */
	public String getCollection() {
		return collection;
	}

	/**
	 * @param collection the collection to set
	 */
	public void setCollection(String collection) {
		this.collection = collection;
	}

	/**
	 * @return the jsonData
	 */
	public String getJsonData() {
		return jsonData;
	}

	/**
	 * @param jsonData the jsonData to set
	 */
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	/* (non-Javadoc)
	 * @see com.gridsofts.install4j.model.IStep#getStepPane()
	 */
	public JPanel getStepPane() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	/* (non-Javadoc)
	 * @see com.gridsofts.install4j.model.IStep#onPreviousStep()
	 */
	public boolean onPreviousStep() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.gridsofts.install4j.model.IStep#onNextStep()
	 */
	public boolean onNextStep() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.gridsofts.install4j.model.IStep#onFinish()
	 */
	public boolean onFinish() {
		// TODO Auto-generated method stub
		return false;
	}

}

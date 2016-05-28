/*
 * 版权所有 ©2011-2016 格点软件(北京)有限公司 All rights reserved.
 * 
 * 未经书面授权，不得擅自复制、影印、储存或散播。
 */
package com.gridsofts.install4j.model;

import java.io.Serializable;

import javax.swing.JPanel;

/**
 * 安装步骤抽象接口
 * 
 * @author lei
 */
public interface IStep extends Serializable {

	/**
	 * 步骤名称
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 用户操作面板
	 * 
	 * @return
	 */
	public JPanel getStepPane();

	/**
	 * 第一个步骤
	 * 
	 * @param value
	 */
	public void setFirst(boolean value);

	/**
	 * 最后一步
	 * 
	 * @param value
	 */
	public void setLast(boolean value);

	/**
	 * 上一步
	 * 
	 * @return
	 */
	public boolean onPreviousStep();

	/**
	 * 下一步
	 * 
	 * @return
	 */
	public boolean onNextStep();

	/**
	 * 完成
	 * 
	 * @return
	 */
	public boolean onFinish();
}

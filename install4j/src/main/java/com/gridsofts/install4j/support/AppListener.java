/*
 * 版权所有 ©2011-2016 格点软件(北京)有限公司 All rights reserved.
 * 
 * 未经书面授权，不得擅自复制、影印、储存或散播。
 */
package com.gridsofts.install4j.support;

import java.util.EventListener;

import org.gridsofts.util.EventObject;

/**
 * 全局的应用事件
 * 
 * @author lei
 */
public interface AppListener extends EventListener {

	/**
	 * 设置工具栏按钮可用
	 * 
	 * @param stateEvt
	 */
	public void setButtonsEnable(EventObject<Integer> stateEvt);
	
	/**
	 * 设置工具栏按钮不可用
	 * 
	 * @param stateEvt
	 */
	public void setButtonsDisable(EventObject<Integer> stateEvt);
}

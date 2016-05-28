/*
 * 版权所有 ©2011-2016 格点软件(北京)有限公司 All rights reserved.
 * 
 * 未经书面授权，不得擅自复制、影印、储存或散播。
 */
package com.gridsofts.install4j.support;

import org.gridsofts.util.EventDispatcher;
import org.gridsofts.util.EventObject;

/**
 * 全局的事件派发器
 * 
 * @author lei
 */
public class AppEventDispatcher extends EventDispatcher<AppListener, EventObject<?>> {
	private static final long serialVersionUID = 1L;

	private static class SingletonHolder {
		private static final AppEventDispatcher instance = new AppEventDispatcher();
	}
	
	public static AppEventDispatcher getInstance() {
		return SingletonHolder.instance;
	}
	
	private AppEventDispatcher() {
	}
}

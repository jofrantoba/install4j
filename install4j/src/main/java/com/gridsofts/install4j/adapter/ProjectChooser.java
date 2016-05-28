/*
 * 版权所有 ©2011-2016 格点软件(北京)有限公司 All rights reserved.
 * 
 * 未经书面授权，不得擅自复制、影印、储存或散播。
 */
package com.gridsofts.install4j.adapter;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.JPanel;

import com.gridsofts.install4j.model.IStep;
import com.gridsofts.install4j.model.Project;

/**
 * @author lei
 */
public class ProjectChooser extends JPanel implements IStep {
	private static final long serialVersionUID = 1L;
	
	private boolean isFirst, isLast;
	
	private Map<String, Project> proMap;
	
	public ProjectChooser(Map<String, Project> proMap) {
		super(new BorderLayout());
		
		this.proMap = proMap;
		
		setName("版本选择");
	}

	@Override
	public JPanel getStepPane() {
		return null;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	@Override
	public boolean onPreviousStep() {
		return false;
	}

	@Override
	public boolean onNextStep() {
		return false;
	}

	@Override
	public boolean onFinish() {
		return false;
	}

}

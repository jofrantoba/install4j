/*
 * 版权所有 ©2011-2016 格点软件(北京)有限公司 All rights reserved.
 * 
 * 未经书面授权，不得擅自复制、影印、储存或散播。
 */
package com.gridsofts.install4j.adapter;

import java.io.Serializable;
import java.util.List;

import javax.swing.JPanel;

import com.gridsofts.install4j.model.IStep;

/**
 * 本地配置
 * 
 * @author lei
 */
public class Properties implements IStep {
	private static final long serialVersionUID = 1L;
	
	private boolean isFirst, isLast;

	private String name;
	private String file;

	private List<Entry> entryList;
	private List<Entry> advanceList;

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
	 * @return the file
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * @return the entryList
	 */
	public List<Entry> getEntryList() {
		return entryList;
	}

	/**
	 * @param entryList the entryList to set
	 */
	public void setEntryList(List<Entry> entryList) {
		this.entryList = entryList;
	}

	/**
	 * @return the advanceList
	 */
	public List<Entry> getAdvanceList() {
		return advanceList;
	}

	/**
	 * @param advanceList the advanceList to set
	 */
	public void setAdvanceList(List<Entry> advanceList) {
		this.advanceList = advanceList;
	}

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gridsofts.install4j.model.IStep#onPreviousStep()
	 */
	public boolean onPreviousStep() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gridsofts.install4j.model.IStep#onNextStep()
	 */
	public boolean onNextStep() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gridsofts.install4j.model.IStep#onFinish()
	 */
	public boolean onFinish() {
		// TODO Auto-generated method stub
		return false;
	}

	public static class Entry implements Serializable {
		private static final long serialVersionUID = 1L;

		private String name;
		private String key;
		private String value;

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
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @param key
		 *            the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value
		 *            the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}
	}
}

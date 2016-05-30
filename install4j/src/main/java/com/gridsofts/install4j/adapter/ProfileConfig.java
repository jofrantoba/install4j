/*
 * 版权所有 ©2011-2016 格点软件(北京)有限公司 All rights reserved.
 * 
 * 未经书面授权，不得擅自复制、影印、储存或散播。
 */
package com.gridsofts.install4j.adapter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.gridsofts.swing.JForm;
import org.gridsofts.swing.VerticalLayoutPane;
import org.gridsofts.util.EventObject;

import com.gridsofts.install4j.model.IStep;
import com.gridsofts.install4j.support.AppEventDispatcher;
import com.gridsofts.install4j.support.AppListener;

/**
 * 本地配置
 * 
 * @author lei
 */
public class ProfileConfig implements IStep, AppListener {
	private static final long serialVersionUID = 1L;

	private StepPane pane = null;

	private boolean isFirst, isLast;

	private String name;
	private String file;

	private File installDirectory;

	private List<Entry> entryList;
	private List<Entry> advanceList;

	public ProfileConfig() {

		AppEventDispatcher.getInstance().addEventListener(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public List<Entry> getEntryList() {
		return entryList;
	}

	public void setEntryList(List<Entry> entryList) {
		this.entryList = entryList;
	}

	public List<Entry> getAdvanceList() {
		return advanceList;
	}

	public void setAdvanceList(List<Entry> advanceList) {
		this.advanceList = advanceList;
	}

	public JPanel getStepPane() {

		if (pane == null) {
			pane = new StepPane(entryList, advanceList);
		}

		return pane;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public boolean onPreviousStep() {
		return true;
	}

	public boolean onNextStep() {
		return true;
	}

	public boolean onFinish() {

		File destPropertiesFile = new File(installDirectory.getPath() + "/" + file);
		if (!destPropertiesFile.getParentFile().exists()) {
			destPropertiesFile.getParentFile().mkdirs();
		}
		
		Properties properties = new Properties();

		try (FileInputStream inStream = new FileInputStream(destPropertiesFile)) {
			properties.load(inStream);
		} catch (Throwable e) {
		}

		//
		if (pane.getFldList() != null) {
			for (JTextField inputFld : pane.getFldList()) {
				properties.put(inputFld.getName(), inputFld.getText());
			}
		}
		
		try (FileOutputStream outStream = new FileOutputStream(destPropertiesFile)) {
			properties.store(outStream, "Edit by install4j@gridsofts.com");
			return true;
		} catch (Throwable e) {
		}

		return true;
	}

	@Override
	public void setInstallDirectory(EventObject<File> evt) {
		installDirectory = evt.getPayload();
	}

	@Override
	public void setButtonsEnable(EventObject<Integer> stateEvt) {
	}

	@Override
	public void setButtonsDisable(EventObject<Integer> stateEvt) {
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

	private class StepPane extends JPanel {
		private static final long serialVersionUID = 1L;

		private JTabbedPane contentTabPane;
		private List<JTextField> entryFldList = new ArrayList<>();

		public StepPane(List<Entry> entryList, List<Entry> advanceList) {
			super(new BorderLayout());

			setPreferredSize(new Dimension(600, 400));

			contentTabPane = new JTabbedPane();
			add(contentTabPane, BorderLayout.CENTER);

			// General
			if (entryList != null && !entryList.isEmpty()) {
				initGeneralTab(entryList);
			}
			if (advanceList != null && !advanceList.isEmpty()) {
				initAdvanceTab(advanceList);
			}
		}

		public List<JTextField> getFldList() {
			return entryFldList;
		}

		private void initGeneralTab(List<Entry> entryList) {

			VerticalLayoutPane scrollPane = new VerticalLayoutPane();
			JScrollPane scroller = new JScrollPane(scrollPane);
			scroller.setBorder(BorderFactory.createEmptyBorder());
			contentTabPane.add("常规", scroller);

			JForm entryForm = new JForm(10, 10);
			scrollPane.add(entryForm);

			for (Entry entry : entryList) {
				JTextField inputfld = new JTextField();
				inputfld.setName(entry.key);
				inputfld.setText(entry.getValue());

				entryFldList.add(inputfld);
				entryForm.addFormItem(entry.getName(), inputfld);
			}
		}

		private void initAdvanceTab(List<Entry> entryList) {

			VerticalLayoutPane scrollPane = new VerticalLayoutPane();
			JScrollPane scroller = new JScrollPane(scrollPane);
			scroller.setBorder(BorderFactory.createEmptyBorder());
			contentTabPane.add("高级", scroller);

			JForm entryForm = new JForm(10, 10);
			scrollPane.add(entryForm);

			for (Entry entry : entryList) {
				JTextField inputfld = new JTextField();
				inputfld.setName(entry.key);
				inputfld.setText(entry.getValue());

				entryFldList.add(inputfld);
				entryForm.addFormItem(entry.getName(), inputfld);
			}
		}
	}
}

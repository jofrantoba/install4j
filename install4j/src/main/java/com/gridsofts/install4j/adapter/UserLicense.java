/*
 * 版权所有 ©2011-2016 格点软件(北京)有限公司 All rights reserved.
 * 
 * 未经书面授权，不得擅自复制、影印、储存或散播。
 */
package com.gridsofts.install4j.adapter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gridsofts.util.EventObject;
import org.gridsofts.util.FileIO;

import com.gridsofts.install4j.model.IStep;
import com.gridsofts.install4j.support.AppEventDispatcher;

/**
 * 用户许可协议
 * 
 * @author lei
 */
public class UserLicense implements IStep {
	private static final long serialVersionUID = 1L;

	private boolean isFirst, isLast;

	private LicensePane pane = null;

	private String name; // 步骤名称
	private String file; // 协议文件(相对路径)

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

	/**
	 * 获取用户操作面板
	 */
	public JPanel getStepPane() {

		if (pane == null) {
			pane = new LicensePane(file);
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
		return false;
	}

	public boolean onNextStep() {

		if (pane != null) {
			return pane.isAgree();
		}

		return false;
	}

	public boolean onFinish() {
		return false;
	}

	private class LicensePane extends JPanel implements ChangeListener {
		private static final long serialVersionUID = 1L;

		private JTextArea licViewer;
		private JCheckBox agreeChk;

		public LicensePane(String licfile) {
			super(new BorderLayout());

			setPreferredSize(new Dimension(600, 400));

			licViewer = new JTextArea();
			licViewer.setEditable(false);
			licViewer.setLineWrap(true);

			JScrollPane scroller = new JScrollPane(licViewer);
			add(scroller, BorderLayout.CENTER);

			agreeChk = new JCheckBox("我已认真阅读本用户协议并全部同意");
			add(agreeChk, BorderLayout.SOUTH);
			agreeChk.addChangeListener(this);

			// load license content
			licViewer.setText(FileIO.readString(new File(licfile)));
		}

		public boolean isAgree() {
			return agreeChk.isSelected();
		}

		@Override
		public void stateChanged(ChangeEvent e) {

			int buttonsMask = 0b00100;
			if (UserLicense.this.isLast) {
				buttonsMask = 0b00010;
			}

			if (agreeChk.isSelected()) {
				AppEventDispatcher.getInstance().dispatchEvent("setButtonsEnable",
						new EventObject<>(UserLicense.this, buttonsMask));
			}
			
			else {
				AppEventDispatcher.getInstance().dispatchEvent("setButtonsDisable",
						new EventObject<>(UserLicense.this, buttonsMask));
			}
		}
	}
}

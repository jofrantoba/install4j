package com.gridsofts.install4j.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import org.gridsofts.util.EventObject;

import com.gridsofts.install4j.model.IStep;
import com.gridsofts.install4j.model.Project;
import com.gridsofts.install4j.support.AppEventDispatcher;
import com.gridsofts.install4j.support.AppListener;
import com.gridsofts.install4j.support.Configurator;

/**
 *
 */
public class App extends JFrame implements ActionListener, AppListener {
	private static final long serialVersionUID = 1L;
	
	private static final Integer BtnPreviousMask = 0b01000; 
	private static final Integer BtnNextMask = 0b00100; 
	private static final Integer BtnFinishMask = 0b00010; 
	private static final Integer BtnCancelMask = 0b00001; 
	
	private JPanel contentPane;
	
	private JToolBar controlbar;
	private JButton btnPrevious, btnNext, btnFinish, btnCancel;
	
	private ControlbarActionHandler controlbarHandler;

	private Map<String, Project> proMap;
	private List<IStep> stepList;
	private int stepTotalCount, currentStepIdx;

	public App(String title, Map<String, Project> proMap) {
		super(title);

		this.proMap = proMap;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		contentPane = new JPanel(new BorderLayout());
		getContentPane().add(contentPane, BorderLayout.CENTER);
		
		// 工具栏
		controlbar = new JToolBar();
		getContentPane().add(controlbar, BorderLayout.SOUTH);
		
		controlbar.setFloatable(false);
		controlbar.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		controlbarHandler = new ControlbarActionHandler();
		
		btnPrevious = new JButton("上一步");
		btnPrevious.setEnabled(false);
		btnPrevious.setActionCommand("previous");
		btnPrevious.addActionListener(controlbarHandler);
		
		btnNext = new JButton("下一步");
		btnNext.setEnabled(false);
		btnNext.setActionCommand("next");
		btnNext.addActionListener(controlbarHandler);
		
		btnFinish = new JButton("完成");
		btnFinish.setEnabled(false);
		btnFinish.setActionCommand("finish");
		btnFinish.addActionListener(controlbarHandler);
		
		btnCancel = new JButton("取消");
		btnCancel.setActionCommand("cancel");
		btnCancel.addActionListener(controlbarHandler);
		
		controlbar.add(btnPrevious);
		controlbar.add(btnNext);
		controlbar.add(btnFinish);
		controlbar.addSeparator();
		controlbar.add(btnCancel);

		// 选择版本
		JPanel verChoosePane = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		verChoosePane.setBorder(BorderFactory.createTitledBorder("请选择软件安装版本"));
		contentPane.add(verChoosePane, BorderLayout.CENTER);

		Iterator<String> proNameSets = proMap.keySet().iterator();
		while (proNameSets.hasNext()) {
			String proName = proNameSets.next();

			JButton btnVersion = new JButton();
			btnVersion.setText(proName);
			btnVersion.setActionCommand(proName);
			btnVersion.addActionListener(this);
			btnVersion.setPreferredSize(new Dimension(200, 50));

			verChoosePane.add(btnVersion);
		}

		//
		centralWindow();
		
		// register app listener
		AppEventDispatcher.getInstance().addEventListener(this);
	}
	
	private void centralWindow() {

		pack();
		
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(scr.width / 2 - getWidth() / 2, scr.height / 2 - getHeight() / 2);
	}

	/**
	 * 显示下一个安装步骤
	 */
	private void showNextStep() {
		currentStepIdx++;

		if (currentStepIdx < stepList.size()) {
			IStep currentStep = stepList.get(currentStepIdx);
			
			setTitle(currentStep.getName());

			contentPane.removeAll();
			contentPane.add(currentStep.getStepPane(), BorderLayout.CENTER);

			contentPane.updateUI();

			centralWindow();
		}
	}
	
	/**
	 * 工具栏按钮事件处理方法
	 */
	private class ControlbarActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent evt) {
			
			if ("cancel".equalsIgnoreCase(evt.getActionCommand())) {
				System.exit(0);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {

		String proName = evt.getActionCommand();
		Project project = proMap.get(proName);
		if (project != null) {
			stepList = project.getStepList();

			stepTotalCount = stepList.size();
			currentStepIdx = -1;

			showNextStep();
		}
	}

	@Override
	public Insets getInsets() {
		Insets insets = super.getInsets();
		return new Insets(insets.top + 10, insets.left + 10, insets.bottom + 10, insets.right + 10);
	}

	@Override
	public void setButtonsEnable(EventObject<Integer> stateEvt) {
		
		if (stateEvt != null && stateEvt.getPayload() != null) {
			
			if ((BtnPreviousMask & stateEvt.getPayload()) != 0) {
				btnPrevious.setEnabled(true);
			}
			if ((BtnNextMask & stateEvt.getPayload()) != 0) {
				btnNext.setEnabled(true);
			}
			if ((BtnFinishMask & stateEvt.getPayload()) != 0) {
				btnFinish.setEnabled(true);
			}
			if ((BtnCancelMask & stateEvt.getPayload()) != 0) {
				btnCancel.setEnabled(true);
			}
		}
	}
	
	@Override
	public void setButtonsDisable(EventObject<Integer> stateEvt) {
		
		if (stateEvt != null && stateEvt.getPayload() != null) {
			
			if ((BtnPreviousMask & stateEvt.getPayload()) != 0) {
				btnPrevious.setEnabled(false);
			}
			if ((BtnNextMask & stateEvt.getPayload()) != 0) {
				btnNext.setEnabled(false);
			}
			if ((BtnFinishMask & stateEvt.getPayload()) != 0) {
				btnFinish.setEnabled(false);
			}
			if ((BtnCancelMask & stateEvt.getPayload()) != 0) {
				btnCancel.setEnabled(false);
			}
		}
	}

	/**
	 * 安装向导启动方法
	 * 
	 * @param args
	 *            [ programName ]
	 */
	public static void main(String[] args) {
		String programName = "";

		if (args != null && args.length > 0) {
			programName = args[0] + " - ";
		}

		// 解析项目配置文件
		Map<String, Project> proMap = Configurator.parse(new File("./project.xml"));
		if (proMap != null && !proMap.isEmpty()) {

			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Throwable e) {
			}

			new App(programName + "安装向导", proMap).setVisible(true);
		}
	}
}

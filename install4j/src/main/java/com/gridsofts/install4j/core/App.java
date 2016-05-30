package com.gridsofts.install4j.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.gridsofts.swing.JDialog;
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
	private int currentStepIdx;

	private File installDirectory; // 安装位置
	
	public static App instance = null;

	public App(String title, Map<String, Project> proMap) {
		super(title);
		
		this.proMap = proMap;
		
		instance = this;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPane = new JPanel(new BorderLayout());
		getContentPane().add(contentPane, BorderLayout.CENTER);
		
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

		btnFinish = new JButton("开始安装");
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

	@Override
	public void actionPerformed(ActionEvent evt) {

		String proName = evt.getActionCommand();
		Project project = proMap.get(proName);
		if (project != null) {
			stepList = project.getStepList();

			currentStepIdx = -1;

			showNextStep();
		}
	}

	public File getInstallDirectory() {
		return installDirectory;
	}

	@Override
	public void setInstallDirectory(EventObject<File> evt) {
		installDirectory = evt.getPayload();
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
	
	private void forButtonsState() {
		btnPrevious.setEnabled(currentStepIdx > 0);
		btnNext.setEnabled(currentStepIdx < stepList.size() - 1);
		btnFinish.setEnabled(currentStepIdx == stepList.size() - 1);
	}

	/**
	 * 显示上一个安装步骤
	 */
	private void showPreviousStep() {

		if (currentStepIdx > 0) {
			IStep currentStep = stepList.get(currentStepIdx);

			if (currentStep.onPreviousStep()) {
				IStep previousStep = stepList.get(--currentStepIdx);

				setTitle(previousStep.getName());

				contentPane.removeAll();
				contentPane.add(previousStep.getStepPane(), BorderLayout.CENTER);

				contentPane.updateUI();

				centralWindow();
			}
		}
		
		forButtonsState();
	}

	/**
	 * 显示下一个安装步骤
	 */
	private void showNextStep() {

		if (currentStepIdx < stepList.size() - 1) {
			IStep currentStep = null;
			
			if (currentStepIdx >= 0) {
				currentStep = stepList.get(currentStepIdx);
			}

			if (currentStep == null || currentStep.onNextStep()) {
				IStep nextStep = stepList.get(++currentStepIdx);

				setTitle(nextStep.getName());

				contentPane.removeAll();
				contentPane.add(nextStep.getStepPane(), BorderLayout.CENTER);

				contentPane.updateUI();

				centralWindow();
			}
		}
		
		forButtonsState();
	}

	/**
	 * 安装完成
	 */
	private void onFinish() {

		JDialog progressDlg = new JDialog(App.this, "正在安装，请稍候...", false, 500, 70);
		progressDlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		JProgressBar progress = new JProgressBar();
		progressDlg.getContentPane().add(progress, BorderLayout.CENTER);
		
		progressDlg.setVisible(true);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new InstallTask(progressDlg, progress, stepList.size() + 1).start();
			}
		});
	}
	
	private class InstallTask extends Thread {
		
		private JDialog progressDlg;
		private JProgressBar progress;
		private int totalSteps;
		
		public InstallTask(JDialog progressDlg, JProgressBar progress, int totalSteps) {
			this.progressDlg = progressDlg;
			this.progress = progress;
			this.totalSteps = totalSteps;
		}

		@Override
		public void run() {

			progress.setMaximum(totalSteps);
			progress.setValue(1);
			progress.updateUI();
			
			Iterator<IStep> stepItarator = stepList.iterator();
			while (stepItarator.hasNext()) {
				IStep step = stepItarator.next();

				if (step.onFinish()) {
					progress.setValue(progress.getValue() + 1);
					progress.updateUI();
					
				} else {
					progressDlg.dispose();
					
					JOptionPane.showMessageDialog(App.this, "第 " + progress.getValue() + " 步安装失败");
					return;
				}
			}

			progressDlg.dispose();
			JOptionPane.showMessageDialog(App.this, "安装完毕");
			System.exit(0);
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

			else if ("previous".equalsIgnoreCase(evt.getActionCommand())) {
				showPreviousStep();
			}

			else if ("next".equalsIgnoreCase(evt.getActionCommand())) {
				showNextStep();
			}

			else if ("finish".equalsIgnoreCase(evt.getActionCommand())) {
				onFinish();
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

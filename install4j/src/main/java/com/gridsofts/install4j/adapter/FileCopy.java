/*
 * 版权所有 ©2011-2016 格点软件(北京)有限公司 All rights reserved.
 * 
 * 未经书面授权，不得擅自复制、影印、储存或散播。
 */
package com.gridsofts.install4j.adapter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.gridsofts.swing.tree.JEditableTree;
import org.gridsofts.swing.treeClasses.ITreeNode;
import org.gridsofts.util.EventObject;
import org.gridsofts.util.FileIO;
import org.gridsofts.util.StringUtil;

import com.gridsofts.install4j.core.App;
import com.gridsofts.install4j.model.IStep;
import com.gridsofts.install4j.support.AppEventDispatcher;

/**
 * 文件复制
 * 
 * @author lei
 */
public class FileCopy implements IStep {
	private static final long serialVersionUID = 1L;

	private StepPane pane = null;

	private boolean isFirst, isLast;

	private String name;
	private String source;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public JPanel getStepPane() {

		if (pane == null) {
			pane = new StepPane(source);
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

		if (pane != null) {
			return pane.isChooseDone();
		}

		return false;
	}

	public boolean onFinish() {

		if (pane != null && pane.isChooseDone()) {
			File filePath = new File(pane.getFilePath());
			if (!filePath.exists()) {
				filePath.mkdirs();
			}

			AppEventDispatcher.getInstance().dispatchEvent("setInstallDirectory",
					new EventObject<File>(FileCopy.this, filePath));

			// prepare copy files
			File sourceDir = new File(source);
			try {
				copyFiles(sourceDir, "", filePath.getPath());
				return true;
			} catch (Throwable e) {
			}
		}

		return false;
	}

	private void copyFiles(File file, String dirName, String destination) throws FileNotFoundException, IOException {

		if (file.exists() && file.isFile()) {

			File dest = new File(destination + dirName + file.getName());
			if (!dest.getParentFile().exists()) {
				dest.getParentFile().mkdirs();
			}

			try (FileInputStream inStream = new FileInputStream(file);
					FileOutputStream outStream = new FileOutputStream(dest)) {

				FileIO.copy(inStream, outStream, 2048, true);
			}
		} else if (file.exists() && file.isDirectory()) {
			File[] files = file.listFiles();
			
			for (File f : files) {
				copyFiles(f, dirName + "/" + file.getName(), destination);
			}
		}
	}

	private class StepPane extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;

		private JEditableTree sourceFileTree;
		private JTextField filePathFld;

		public StepPane(String sourceDir) {
			super(new BorderLayout());

			setPreferredSize(new Dimension(600, 400));

			sourceFileTree = new JEditableTree(false);

			JScrollPane scroller = new JScrollPane(sourceFileTree);
			add(scroller, BorderLayout.CENTER);

			JPanel bottomPane = new JPanel(new BorderLayout());
			add(bottomPane, BorderLayout.SOUTH);
			bottomPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

			filePathFld = new JTextField();
			bottomPane.add(filePathFld, BorderLayout.CENTER);
			filePathFld.setPreferredSize(new Dimension(0, 30));

			JButton btnChoose = new JButton("选择安装位置");
			bottomPane.add(btnChoose, BorderLayout.WEST);
			btnChoose.setPreferredSize(new Dimension(150, 30));
			btnChoose.addActionListener(this);

			// Retrieve source files
			File sourceFile = new File(sourceDir);
			if (sourceFile.exists()) {
				sourceFileTree.setRootTreeNode(retrieveSourceDir(sourceFile));
				sourceFileTree.expandAll();
			}
		}

		private FileNode retrieveSourceDir(File sourceFile) {
			FileNode rootNode = new FileNode(sourceFile.getName());

			if (sourceFile.isDirectory()) {
				File[] files = sourceFile.listFiles();
				for (File file : files) {
					rootNode.add(retrieveSourceDir(file));
				}
			}

			return rootNode;
		}

		public String getFilePath() {
			return filePathFld.getText();
		}

		public boolean isChooseDone() {
			return !StringUtil.isNull(filePathFld.getText());
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("选择安装目录");
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(App.instance)) {
				filePathFld.setText(fileChooser.getSelectedFile().getPath());
			}
		}
	}

	private class FileNode implements ITreeNode {

		private String name;

		private List<FileNode> children = new ArrayList<>();

		public FileNode(String name) {
			setName(name);
		}

		@Override
		public String toString() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public void add(ITreeNode child) {
			children.add((FileNode) child);
		}

		@Override
		public void remove(ITreeNode child) {
			children.remove(child);
		}

		@Override
		public void removeAll() {
			children.removeAll(children);
		}

		@Override
		public List<?> getChildren() {
			return children;
		}
	}
}

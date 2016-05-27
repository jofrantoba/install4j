package com.gridsofts.install4j.core;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 *
 */
public class App extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public App(String title) {
		super(title);
		
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(scr.width / 2 - 300, scr.height / 2 - 200, 600, 400);
	}

	/**
	 * 安装向导启动方法
	 * 
	 * @param args [ programName ]
	 */
	public static void main(String[] args) {
		String programName = "";
		
		if (args != null && args.length > 0) {
			programName = args[0];
		}
		
		new App(programName + " - 安装向导").setVisible(true);
	}
}

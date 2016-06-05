/*
 * 版权所有 ©2011-2016 格点软件(北京)有限公司 All rights reserved.
 * 
 * 未经书面授权，不得擅自复制、影印、储存或散播。
 */
package com.gridsofts.install4j.adapter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.bson.Document;
import org.gridsofts.util.StringUtil;

import com.gridsofts.install4j.model.IStep;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * MongoDB数据库初始化
 * 
 * @author lei
 */
public class MongoDataInit implements IStep {
	private static final long serialVersionUID = 1L;

	private StepPane pane = null;

	private boolean isFirst, isLast;

	private String name;
	private String jsonData;

	private String host;
	private int port;
	private String dbname;
	private String collection;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public JPanel getStepPane() {

		if (pane == null) {
			pane = new StepPane(jsonData);
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

		if (StringUtil.isEmpty(jsonData)) {
			return true;
		}

		try (MongoClient mongoCli = new MongoClient(host, port);) {
			MongoDatabase mongoDb = mongoCli.getDatabase(dbname);

			MongoCollection<Document> collec = mongoDb.getCollection(collection);
			if (collec != null) {

				try (ByteArrayInputStream byteInStream = new ByteArrayInputStream(jsonData.getBytes());
						BufferedReader dataReader = new BufferedReader(new InputStreamReader(byteInStream));) {

					String temp = null;
					while ((temp = dataReader.readLine()) != null) {
						collec.insertOne(Document.parse(temp));
					}

					return true;
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return false;
	}

	private class StepPane extends JPanel {
		private static final long serialVersionUID = 1L;

		private JTextArea dataViewer;

		public StepPane(String jsonData) {
			super(new BorderLayout());

			setPreferredSize(new Dimension(600, 400));

			dataViewer = new JTextArea();
			dataViewer.setEditable(false);
			dataViewer.setLineWrap(true);
			dataViewer.setText(jsonData);

			JScrollPane scroller = new JScrollPane(dataViewer);
			add(scroller, BorderLayout.CENTER);
		}
	}
}

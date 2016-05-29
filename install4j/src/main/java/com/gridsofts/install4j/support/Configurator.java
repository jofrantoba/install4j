/*
 * 版权所有 ©2011-2016 格点软件(北京)有限公司 All rights reserved.
 * 
 * 未经书面授权，不得擅自复制、影印、储存或散播。
 */
package com.gridsofts.install4j.support;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gridsofts.install4j.adapter.FileCopy;
import com.gridsofts.install4j.adapter.MongoDataInit;
import com.gridsofts.install4j.adapter.ProfileConfig;
import com.gridsofts.install4j.adapter.UserLicense;
import com.gridsofts.install4j.model.IStep;
import com.gridsofts.install4j.model.Project;

/**
 * 项目配置解析器
 * 
 * @author lei
 */
public class Configurator {

	public static final Deque<Project> proStack = new ArrayDeque<>();
	public static final Deque<String> stepNameStack = new ArrayDeque<>();
	public static final Deque<IStep> eStack = new ArrayDeque<>();
	public static final Deque<StringBuffer> contentStack = new ArrayDeque<>();
	
	public static List<ProfileConfig.Entry> entryList = null;
	public static List<ProfileConfig.Entry> advanceList = null;
	public static List<ProfileConfig.Entry> entryListPointer = null;
	
	public static Map<String, Project> parse(File file) {
		
		Map<String, Project> configurationMap = new HashMap<>();
		
		proStack.clear();
		stepNameStack.clear();
		eStack.clear();
		contentStack.clear();

		try {
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			saxParser.parse(file, new DefaultHandler() {

				@Override
				public void startDocument() throws SAXException {
				}

				@Override
				public void endDocument() throws SAXException {
					Iterator<Project> proIterator = proStack.iterator();
					while (proIterator.hasNext()) {
						Project project = proIterator.next();
						configurationMap.put(project.getName(), project);
					}
				}

				@Override
				public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {

					if ("project".equalsIgnoreCase(qName)) {
						Project project = new Project();
						proStack.push(project);
						
						project.setName(attributes.getValue("name"));
						project.setStepList(new ArrayList<>());
					}
					
					else if ("step".equalsIgnoreCase(qName)) {
						stepNameStack.push(attributes.getValue("name"));
					}

					else if ("license".equalsIgnoreCase(qName)) {
						UserLicense license = new UserLicense();
						eStack.push(license);
						
						license.setName(stepNameStack.pop());
						license.setFile(attributes.getValue("file"));
					}
					
					else if ("file-copy".equalsIgnoreCase(qName)) {
						FileCopy fcopy = new FileCopy();
						eStack.push(fcopy);
						
						fcopy.setName(stepNameStack.pop());
						fcopy.setSource(attributes.getValue("source"));
					}
					
					else if ("mongodb-data".equalsIgnoreCase(qName)) {
						MongoDataInit mongoData = new MongoDataInit();
						eStack.push(mongoData);
						
						mongoData.setName(stepNameStack.pop());
						mongoData.setHost(attributes.getValue("host"));
						
						if (attributes.getIndex("port") >= 0) {
							mongoData.setPort(Integer.parseInt(attributes.getValue("port")));
						}
						mongoData.setDbname(attributes.getValue("dbname"));
						mongoData.setCollection(attributes.getValue("collection"));

						contentStack.push(new StringBuffer());
					}
					
					else if ("profile".equalsIgnoreCase(qName)) {
						ProfileConfig profile = new ProfileConfig();
						eStack.push(profile);
						
						profile.setName(stepNameStack.pop());
						profile.setFile(attributes.getValue("file"));
						
						entryList = new ArrayList<>();
						advanceList = new ArrayList<>();
						
						entryListPointer = entryList;
					}
					
					else if ("entry".equalsIgnoreCase(qName)) {
						ProfileConfig.Entry pEntry = new ProfileConfig.Entry();
						entryListPointer.add(pEntry);
						
						pEntry.setName(attributes.getValue("name"));
						pEntry.setKey(attributes.getValue("key"));
						pEntry.setValue(attributes.getValue("value"));
					}
					
					else if ("advance".equalsIgnoreCase(qName)) {
						entryListPointer = advanceList;
					}
				}

				@Override
				public void endElement(String uri, String localName, String qName) throws SAXException {
					
					if ("step".equalsIgnoreCase(qName)) {
						proStack.peek().getStepList().add(eStack.pop());
					}
					
					else if ("advancen".equalsIgnoreCase(qName)) {
						entryListPointer = entryList;
					}
					
					else if ("profile".equalsIgnoreCase(qName)) {
						ProfileConfig theProfile = (ProfileConfig) eStack.peek();
						
						theProfile.setEntryList(entryList);
						theProfile.setAdvanceList(advanceList);
					}
					
					else if ("mongodb-data".equalsIgnoreCase(qName)) {
						((MongoDataInit) eStack.peek()).setJsonData(contentStack.pop().toString().trim());
					}
				}

				@Override
				public void characters(char[] ch, int start, int length) throws SAXException {

					if (length == 0 || contentStack.isEmpty()) {
						return;
					}
					
					StringBuffer buf = contentStack.peek();
					buf.append(new String(ch, start, length));
				}
			});
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		} catch (IOException e) {
		}
		
		return configurationMap;
	}
}

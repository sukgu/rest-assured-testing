package com.framework.utils;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Config {

	public static String URI;
	public static String Username;
	public static String Password;
	public static String ConfigPropertyFilePath;
	public static String TestData;
	public static String OwnerId;
	public static String TenantId;
	public static String mailSender;
	public static String senderPassword;
	public static String mailReciever;
	public static String mailSubject;
	
	static public void loadConfiguration() {
		String file="config/config.properties";
		URI=PropertyUtil.readProperty(file, "application.url");
		Username=PropertyUtil.readProperty(file, "application.username");
		Password=PropertyUtil.readProperty(file, "application.password");
		OwnerId=PropertyUtil.readProperty(file, "application.ownerid");
		TenantId=PropertyUtil.readProperty(file, "application.tenantid");
		TestData=PropertyUtil.readProperty(file, "application.file.testdata");
		mailSender=PropertyUtil.readProperty(file, "application.mail.sender");
		senderPassword=PropertyUtil.readProperty(file, "application.mail.sender.password");
		mailReciever=PropertyUtil.readProperty(file, "application.mail.reciever");
		mailSubject=PropertyUtil.readProperty(file, "application.mail.subject");
	}
	
	static public String readXML(String testdata) {
		try {
			File file = new File(TestData);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			String callerClassName = new Exception().getStackTrace()[1].getClassName();
			int index=new Exception().getStackTrace()[1].getClassName().lastIndexOf('.');
			callerClassName=callerClassName.substring(index+1);
			NodeList nodeList1 = doc.getElementsByTagName("testcase");
			for (int k=0;k<nodeList1.getLength();k++) {
				if(nodeList1.item(k).getNodeType()==Node.ATTRIBUTE_NODE) {
					Element ele=(Element)nodeList1.item(k);
					if(ele.getAttribute("name") == callerClassName) {
						NodeList nodeList2=ele.getChildNodes();
						for (int i=0;i<nodeList2.getLength();i++) {
							  Node node=nodeList2.item(i);
							  if(node.getNodeValue().equals(testdata)) {
								  NodeList n=node.getParentNode().getChildNodes();
								  return n.item(1).getNodeValue();
								  }
							  }
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				}
		return "";
		}
	}
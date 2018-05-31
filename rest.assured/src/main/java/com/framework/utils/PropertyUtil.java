package com.framework.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertyUtil {

	/*private File file;
	private String strfile;*/
	private static Properties properties=new Properties();
	private static OutputStream output=null;
	private static InputStream input=null;
	
	public static String readProperty(String file,String property)
	{
		try {
			input=new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		try {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(!input.equals(null)) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties.getProperty(property);
	}
	
	public static boolean writeProperty(String file,String property,String value)
	{
		try {
			output=new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		properties.setProperty(property,value);
		try {
			properties.store(output,null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean appendPropertyValue(String file,String property,String value) 
	{
		String oldValue=null;
		if(properties.get(property).equals(null)) {
			return writeProperty(file,property,value);
		} else {
			oldValue=readProperty(file, property);
			return writeProperty(file, property, oldValue+";"+value);
		}
	}
	
	public static String readAndDeleteProperty(String file,String property) 
	{
		String value=readProperty(file, property);
		String newValue=null;
		String[] arrProperty=null;
		String read=null;
		if(value.equals(null)) {
			
		} else {
			if(value.contains(";")) {
				arrProperty=value.split(";");
			}
			read=arrProperty[0];
			for (int i=1;i<arrProperty.length;i++) {
				if(newValue.equals(null)) {
					newValue=arrProperty[i];
				} else {
					newValue=newValue+";"+arrProperty[i];
				}
				
			}
			writeProperty(file, property, value);
		}
		return read;
	}
}

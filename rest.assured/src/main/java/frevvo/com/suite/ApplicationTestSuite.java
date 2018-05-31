package frevvo.com.suite;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.framework.utils.LogUtil;
import com.framework.utils.MailUtil;

import frevvo.com.rest.script.ApplicationAPITest;
import frevvo.com.rest.script.DeleteApplication;
import frevvo.com.rest.script.InsertNewApplication;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   ApplicationAPITest.class,
   DeleteApplication.class,
   InsertNewApplication.class
})

public class ApplicationTestSuite {
	
	@BeforeClass
	public static void beforeTest() {
		
	}
	 	@AfterClass
	 	public static void send_mail() {
	 		
	 		Map<String, List<Description>> result_data=null;
	 		ObjectInputStream is=null;
	 		List<Description> passed=null;
	 		List<Description> failed=null;
	 		FileInputStream fis=null;
			try {
				fis = new FileInputStream(System.getProperty("user.home")+"/test.ser");
			} catch (FileNotFoundException e2) {
				e2.printStackTrace();
			}
			BufferedInputStream bis=new BufferedInputStream(fis);
			
	 		try {
				is=new ObjectInputStream(bis);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			while(true) {
				try {
					try {
						result_data=(Map<String, List<Description>>)is.readObject();
					} catch(EOFException e) {
						break;
					}
				}
					catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				passed=result_data.get("passed");
	 			failed=result_data.get("failed");
			}
	 		
	 		
			
			String html="";
			for (Description description : passed) {
				html+="<tr bgcolor='#d3d3d3'><td>"+description.getMethodName()+"</td><td>PASSED</td></tr>";
			}
			for (Description description : failed) {
				html+="<tr bgcolor='#e0ffff'><td>"+description.getMethodName()+"</td><td>FAILED</td><td>"+description.toString()+"</td></tr>";
			}
			String text="<div>"
						+"<table bgcolor='#5f9ea0' border='0' style='width:60%' cellpadding='1' cellspacing='2'>"
						+"<tr bgcolor='#DBD9AD'><td><b>Name</b></td><td><b>Status</b></td></tr>"
						+html
						+"</table></div>";
			
			MailUtil.sendMail(text);
			
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				Files.deleteIfExists(FileSystems.getDefault().getPath(System.getProperty("user.home")+"/test.ser"));
			} catch (IOException e) {
				e.printStackTrace();
			}
	 	}
}

package frevvo.com.suite;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
   public static void main(String[] args) {
	  
	   @SuppressWarnings("rawtypes")
	   Class suite=null;
	   try {
		   suite=Class.forName("frevvo.com.suite."+args[0]);
		   } catch (ClassNotFoundException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			   }
	   
	   Result result = JUnitCore.runClasses(suite);
	   
	   for (Failure failure : result.getFailures()) {
		   System.out.println(failure.getTrace());
		   }
	   if(result.wasSuccessful()) {
		   System.out.println("Test Passed Successfully...");
	   } else {
		   System.out.println("Test Failed...");
	   }
	   
   }
}  	
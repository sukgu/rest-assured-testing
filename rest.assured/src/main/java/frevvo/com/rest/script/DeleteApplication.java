package frevvo.com.rest.script;

import static com.framework.utils.Config.OwnerId;
import static com.framework.utils.Config.TenantId;
import static com.jayway.restassured.RestAssured.authentication;
import static com.jayway.restassured.RestAssured.baseURI;
import static com.jayway.restassured.RestAssured.basic;
import static com.jayway.restassured.RestAssured.given;
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import com.framework.utils.AppendableSerilization;
import com.framework.utils.Config;
import com.framework.utils.LogUtil;
import com.framework.utils.TestWatcherLoggingRule;

public class DeleteApplication extends TestWatcherLoggingRule {

	String applicationID=null;
	String xml="<entry xmlns='http://www.w3.org/2005/Atom'><title type='text'>Expense Reports</title><summary type='text'/></entry>";
	
	@Rule
	public TestRule watcher = new TestWatcherLoggingRule();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Config.loadConfiguration();
		LogUtil.startTestCase(DeleteApplication.class.getSimpleName());
	}
	
	@AfterClass
	public static void setUpAfterClass() throws Exception {
		LogUtil.endTestCase(DeleteApplication.class.getSimpleName());
		try
		 {
			 Map<String, List<Description>> result_data=new HashMap<String, List<Description>>();
			 result_data.put("passed", passed);
			 result_data.put("failed", failed);
			 ObjectOutputStream oos=AppendableSerilization.getObjectOutputStream(new File(System.getProperty("user.home")+"/test.ser"));
			 oos.writeObject(result_data);
			 oos.flush();
			 oos.close();
		  }
		  catch (Exception e)
		  { e. printStackTrace(); }
	}
	
	@Before
	public void setUp() throws Exception {
		authentication = basic(Config.Username,Config.Password);
		baseURI=Config.URI;
		//applicationID=readAndDeleteProperty(ConfigPropertyFilePath, "frevvo.application.id");
		//applicationID="_z5SOkOzbEeSfldSLKufO3g!Designer_Test";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDeleteApplicaction() {
		applicationID=given()
			.contentType("application/atom+xml")
			.body(xml)
		.when()
			.post("/web/tn/"+TenantId+"/api/apps?ownerId="+OwnerId)
		.then()
			.statusCode(200)
			.extract().xmlPath().getString("entry.id");
		
		given()
			
		.when()
			.delete("/web/tn/test.com/api/app/"+applicationID)
		.then()
			.statusCode(200);
	}
	
	@Test
	public void testDeleteWithWrongApplicatinID() {
		String text=given()
	
		.when()
			.delete("/web/tn/test.com/api/app/"+applicationID+12345)
		.then()
			.statusCode(404)
			.extract().body().asString();
		assert(text.equals("Entry '"+applicationID+12345+"' not found"));
	}

}

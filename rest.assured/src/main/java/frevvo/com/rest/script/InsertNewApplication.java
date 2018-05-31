package frevvo.com.rest.script;

import static com.jayway.restassured.RestAssured.authentication;
import static com.jayway.restassured.RestAssured.baseURI;
import static com.jayway.restassured.RestAssured.basic;
import static com.jayway.restassured.RestAssured.given;
import static com.framework.utils.Config.*;
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


public class InsertNewApplication extends TestWatcherLoggingRule {

	String applicationName=Config.readXML("ApplicationName");
	String corruptApplicationName=Config.readXML("CorruptApplicationName");
	String xml="<entry xmlns='http://www.w3.org/2005/Atom'><title type='text'>"+ applicationName +"</title><summary type='text'/></entry>";
	String corruptXml="<entry xmlns='http://www.w3.org/2005/Atom'><title type='text'>"+ corruptApplicationName +"</title><summary type='text'/></entry>";
	
	@Rule
	public TestRule watcher = new TestWatcherLoggingRule();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Config.loadConfiguration();
		LogUtil.startTestCase(InsertNewApplication.class.getSimpleName());
	}
	
	@AfterClass
	public static void setUpAfterClass() throws Exception {
		LogUtil.endTestCase(InsertNewApplication.class.getSimpleName());
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
		authentication = basic(Username,Password);
		baseURI=URI;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInsert() {
		given()
			.contentType("application/atom+xml")
			.body(xml)
		.when()
			.post("/web/tn/"+Config.TenantId+"/api/apps?ownerId="+Config.OwnerId)
		.then()
			.statusCode(200)
			.assertThat().extract().xmlPath().get("entry.id");
		
	}
	
	@Test
	public void testInsertWithWrongOwnerId() {
		given()
			.contentType("application/atom+xml")
			.body(xml)
		.when()
			.post("/web/tn/frevvo_test.com/api/apps?ownerId=WrongOwnerId")
		.then()
			.statusCode(400);
	}
	
	@Test
	public void testInsertWithWrongTenantId() {
		given()
			.contentType("application/atom+xml")
			.body(xml)
		.when()
			.post("/web/tn/wrongTenantId/api/apps?ownerId=ownerId")
		.then()
			.statusCode(404);
	}
	
	@Test
	public void testInsertWithSpecialCharacter() {
		Boolean flag=given()
			.contentType("application/atom+xml")
			.body(corruptXml)
		.when()
			.post("/web/tn/test.com/api/apps?ownerId=ownerId")
		.then()
			.statusCode(400)
			.extract().body().asString().contains("SAXParseException");
		//System.out.println(flag);
		assert(flag);
	}

}

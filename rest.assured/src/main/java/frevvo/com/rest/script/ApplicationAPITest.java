package frevvo.com.rest.script;

import static com.jayway.restassured.RestAssured.authentication;
import static com.jayway.restassured.RestAssured.baseURI;
import static com.jayway.restassured.RestAssured.basic;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;
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

public class ApplicationAPITest extends TestWatcherLoggingRule {

	@Rule
	public TestRule watcher = new TestWatcherLoggingRule();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Config.loadConfiguration();
		LogUtil.startTestCase(ApplicationAPITest.class.getSimpleName());
	}
	
	@AfterClass
	public static void setUpAfterClass() throws Exception {
		
		LogUtil.endTestCase(ApplicationAPITest.class.getSimpleName());
	
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
	public void testApplications() {
		given().when()
			.get("/web/tn/test.com/api/apps")
			.then()
				.assertThat().body("feed.title", equalTo("Applications"))
				.statusCode(200);
	}
	
	@Test
	public void testApplicationList() {
		List<Object> l=get("/web/tn/test.com/api/apps").xmlPath().getList("feed.entry.title");
		assert(l.size()>0);
		//given()
		//.log().all();
		
	}
	
	@Test
	public void testWrongURI() {
		given().when()
			.get("/web/tn/test.com/api/app")
			.then()
				.assertThat().statusCode(404);
				//.log().all();
	}
		
		

}

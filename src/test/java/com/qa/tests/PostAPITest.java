package com.qa.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.base.TestBase;
import com.qa.client.RestClient;
import com.qa.data.Users;
import com.qa.util.TestUtil;

public class PostAPITest extends TestBase{
	
	TestBase testBase;
	RestClient restClient;
	String ServiceUrl;
	String apiUrl;
	String url;
	CloseableHttpResponse httpresponse;
	
	
	@BeforeMethod
	public void SetUp(){
		testBase= new TestBase();
		restClient= new RestClient();
		ServiceUrl= prop.getProperty("URL");
		apiUrl=prop.getProperty("ServiceURL");
		url= ServiceUrl+apiUrl;
			
	}
	
	@Test
	public void postAPITest() throws ClientProtocolException, IOException {
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("username", "test@amazon.com");
//		headerMap.put("password", "test213");
//		headerMap.put("Auth Token", "12345");
		
		//Jackson API
		ObjectMapper mapper=new ObjectMapper();
		Users users=new Users("morpheus","leader"); //Expected users object
		
		//Java object to JSON file conversion - Marshaling
		mapper.writeValue(new File(System.getProperty("user.dir")+"/src/main/java/com/qa/data/users.json"), users);
		
		//Java object to JSON in String - Marshaling
		String usersJsonString= mapper.writeValueAsString(users);
		System.out.println(usersJsonString);
		
		httpresponse= restClient.post(url,usersJsonString, headerMap);
		
		//Validate response from API
		//a. Status code
				int StatusCode= httpresponse.getStatusLine().getStatusCode();
				System.out.println("Status code ====>  " +StatusCode );
				
				Assert.assertEquals(StatusCode, Resp_Status_Code_201, "Status code is not 201");
								
		//b. Response
				String responseString= EntityUtils.toString(httpresponse.getEntity(),"UTF-8");
				
				//JSON String
				JSONObject responseJson= new JSONObject(responseString);
				System.out.println("Response json from api ====>  " +responseJson);
				
				//JSON to java object  - UnMarshaling
				Users usersRespObj= mapper.readValue(responseString,Users.class); //Actual users object
				System.out.println(usersRespObj);
				
				Assert.assertTrue(users.getName().equals(usersRespObj.getName()));
				Assert.assertTrue(users.getJob().equals(usersRespObj.getJob()));
				
				System.out.println(usersRespObj.getId());
				System.out.println(usersRespObj.getCreatedAt());
				

	}
	

}

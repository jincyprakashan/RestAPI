package com.qa.tests;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qa.base.TestBase;
import com.qa.client.RestClient;
import com.qa.util.TestUtil;

public class GetAPITest extends TestBase{
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
	
	@Test(priority=1)
	public void getAPITestWithoutHeader() throws ClientProtocolException, IOException {
		httpresponse= restClient.get(url);
		//a. Status code
		int StatusCode= httpresponse.getStatusLine().getStatusCode();
		System.out.println("Status code ====>  " +StatusCode );
		
		Assert.assertEquals(StatusCode, Resp_Status_Code_200, "Status code is not 200");

		//b. Response
		String responseString= EntityUtils.toString(httpresponse.getEntity(),"UTF-8");
		JSONObject responseJson= new JSONObject(responseString);
		System.out.println("Response json from api ====>  " +responseJson);
		
		//per_page:
		String perPageValue = TestUtil.getValueByJPath(responseJson, "/per_page" );
		System.out.println("PerPageValue is ====> "+perPageValue);
		Assert.assertEquals(Integer.parseInt(perPageValue), 6);
		
		//Total:
		String totalValue = TestUtil.getValueByJPath(responseJson, "/total" );
		System.out.println("Total is ====> "+totalValue);
		Assert.assertEquals(Integer.parseInt(totalValue), 12);
		
		//Get the value from JSON array:
		String firstName= TestUtil.getValueByJPath(responseJson, "/data[0]/first_name" );
		String lastName= TestUtil.getValueByJPath(responseJson, "/data[0]/last_name" );
		String id= TestUtil.getValueByJPath(responseJson, "/data[0]/id" );
		String avatar= TestUtil.getValueByJPath(responseJson, "/data[0]/avatar" );
		
		System.out.println(firstName);
		System.out.println(lastName);
		System.out.println(id);
		System.out.println(avatar);
		
		Assert.assertEquals(firstName, "George");
		
		//c. Header
		Header[] headerArray=httpresponse.getAllHeaders();
		HashMap allHeaders= new HashMap<String,String>();

		for(Header header :headerArray) {
			allHeaders.put(header.getName(), header.getValue());
		}

		System.out.println("Headers Array ====>  "+allHeaders);

	}

	@Test(priority=2)
	public void getAPITestWithHeaders() throws ClientProtocolException, IOException {
		
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("username", "test@amazon.com");
//		headerMap.put("password", "test213");
//		headerMap.put("Auth Token", "12345");
		
		httpresponse= restClient.get(url,headerMap);
		
		//a. Status code
		int StatusCode= httpresponse.getStatusLine().getStatusCode();
		System.out.println("Status code ====>  " +StatusCode );
		
		Assert.assertEquals(StatusCode, Resp_Status_Code_200, "Status code is not 200");

		//b. Response
		String responseString= EntityUtils.toString(httpresponse.getEntity(),"UTF-8");
		JSONObject responseJson= new JSONObject(responseString);
		System.out.println("Response json from api ====>  " +responseJson);
		
		//per_page:
		String perPageValue = TestUtil.getValueByJPath(responseJson, "/per_page" );
		System.out.println("PerPageValue is ====> "+perPageValue);
		Assert.assertEquals(Integer.parseInt(perPageValue), 6);
		
		//Total:
		String totalValue = TestUtil.getValueByJPath(responseJson, "/total" );
		System.out.println("Total is ====> "+totalValue);
		Assert.assertEquals(Integer.parseInt(totalValue), 12);
		
		//Get the value from JSON array:
		String firstName= TestUtil.getValueByJPath(responseJson, "/data[0]/first_name" );
		String lastName= TestUtil.getValueByJPath(responseJson, "/data[0]/last_name" );
		String id= TestUtil.getValueByJPath(responseJson, "/data[0]/id" );
		String avatar= TestUtil.getValueByJPath(responseJson, "/data[0]/avatar" );
		
		System.out.println(firstName);
		System.out.println(lastName);
		System.out.println(id);
		System.out.println(avatar);
		
		Assert.assertEquals(firstName, "George");
		
		//c. Header
		Header[] headerArray=httpresponse.getAllHeaders();
		HashMap allHeaders= new HashMap<String,String>();

		for(Header header :headerArray) {
			allHeaders.put(header.getName(), header.getValue());
		}

		System.out.println("Headers Array ====>  "+allHeaders);

	}


}

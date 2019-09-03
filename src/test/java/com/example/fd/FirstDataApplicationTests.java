package com.example.fd;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.fd.auth.HMACAuth;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FirstDataApplicationTests {

	@Test
	public void contextLoads() {
		HttpResponse response = null;
		HMACAuth fd = new HMACAuth();
		JsonParser parser =  new JsonParser();
		try {
		  response = fd.doGet("/marketplace/v1/categories");
		  int statusCode = response.getStatusLine().getStatusCode();
		  String response_string = EntityUtils.toString(response.getEntity());
		  JsonElement element = parser.parse(response_string);
		  System.out.println("json:" + element);
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}

}

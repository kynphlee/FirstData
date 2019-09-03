package com.example.fd.auth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HMACAuth {
	
	@Value("${fd.username}")
	private String username;
	
	@Value("${fd.secret}")
	private String secret;
	
	@Value("${fd.apiurl}")
	private String apiUrl;
	
	private String formattedDate;
	private String hmacToken;
	private String authHeader;
	
	private static class HmacHelpers {
		private Header oauthHeader;
		private Header dateHeader;
		private String ServiceURL;
		
		public Header getOauthHeader() {
			return oauthHeader;
		}
		public void setOauthHeader(Header oauthHeader) {
			this.oauthHeader = oauthHeader;
		}
		public Header getDateHeader() {
			return dateHeader;
		}
		public void setDateHeader(Header dateHeader) {
			this.dateHeader = dateHeader;
		}
		public String getServiceURL() {
			return ServiceURL;
		}
		public void setServiceURL(String serviceURL) {
			ServiceURL = serviceURL;
		}
	}
	
	public HttpResponse doGet(String uri) throws Exception {
		HmacHelpers helpers = prepareHeaders(uri);
		
		HttpClient httpClientLead = HttpClientBuilder.create().build();
	    HttpGet httpGet = new HttpGet(helpers.getServiceURL());
	    httpGet.addHeader(helpers.getOauthHeader());
	    httpGet.addHeader(helpers.getDateHeader());
	    HttpResponse response = httpClientLead.execute(httpGet);
	    return response;
	}
	
	public HttpResponse doPost(String uri, StringEntity payload) throws Exception {
		HmacHelpers helpers = prepareHeaders(uri);
		
		HttpClient httpClientLead = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(helpers.getServiceURL());
		httpPost.addHeader(helpers.getOauthHeader());
		httpPost.addHeader(helpers.getDateHeader());
		
		httpPost.addHeader("Content-Type","application/json");
		httpPost.setEntity(payload);
		HttpResponse response = httpClientLead.execute(httpPost);
		return response;
	}
	
	private HmacHelpers prepareHeaders(String uri) {
		formattedDate = getCurrentGMTDate();
		hmacToken = generateHMACToken(formattedDate, secret);
		authHeader = "hmac username=\""+username+"\", algorithm=\"hmac-sha1\", headers=\"date\", signature=\""+hmacToken+"\"";
		
		HmacHelpers helpers = new HmacHelpers();
		helpers.setOauthHeader(new BasicHeader("authorization", authHeader ));
		helpers.setDateHeader(new BasicHeader("date", formattedDate));
		helpers.setServiceURL(apiUrl+uri);
		
		return helpers;
	}
	
	// method to be used in doGet and doPost methods above
	private String getCurrentGMTDate(){
	  Date curDate = new Date();
	  SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
	  format.setTimeZone(TimeZone.getTimeZone("GMT"));
	  String formattedDate = format.format(curDate);
	  return formattedDate;
	}
	
	// method to be used in doGet and doPost methods above
	public String generateHMACToken (String formattedDate, String secret){
	  String authorizeString = Base64.encodeBase64String(HmacUtils.hmacSha1(secret, "date: "+formattedDate));
	  return authorizeString;
	}
}

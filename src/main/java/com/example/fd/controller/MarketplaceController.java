package com.example.fd.controller;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fd.auth.HMACAuth;

@RestController
@RequestMapping("/api")
public class MarketplaceController {
	
	@Autowired
	private HMACAuth auth;
	
	private static class ApiHelpers {
		private int statusCode;
		private String response;
		
		public int getStatusCode() {
			return statusCode;
		}
		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}
		public String getResponse() {
			return response;
		}
		public void setResponse(String response) {
			this.response = response;
		}
	}
	
	private ApiHelpers processGet(String uri) {
		ApiHelpers helpers = new ApiHelpers();
		HttpResponse response;
		try {
			response = auth.doGet(uri);
			helpers.setStatusCode(response.getStatusLine().getStatusCode());
			helpers.setResponse(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return helpers;
	}

	@GetMapping(path = "/marketplace/v1/categories", produces = "application/json")
	public ResponseEntity<String> categories() {
		ApiHelpers result = processGet("/marketplace/v1/categories");
		return ResponseEntity.status(result.getStatusCode()).body(result.getResponse());
	}
	
	@GetMapping(path = "/marketplace/v1/products", produces = "application/json")
	public ResponseEntity<String> products() {
		ApiHelpers result = processGet("/marketplace/v1/products");
		return ResponseEntity.status(result.getStatusCode()).body(result.getResponse());
	}
	
	@GetMapping(path = "/marketplace/v1/products/{productId}/details", produces = "application/json")
	public ResponseEntity<String> productDetails(@PathVariable("productId") String productId) {
		String requestStr = String.format("/marketplace/v1/products/%s/details", productId);
		ApiHelpers result = processGet(requestStr);
		return ResponseEntity.status(result.getStatusCode()).body(result.getResponse());
	}
	
	@GetMapping(path = "/marketplace/v1/products/{productId}/features", produces = "application/json")
	public ResponseEntity<String> productFeatures(@PathVariable("productId") String productId) {
		String requestStr = String.format("/marketplace/v1/products/%s/features", productId);
		ApiHelpers result = processGet(requestStr);
		return ResponseEntity.status(result.getStatusCode()).body(result.getResponse());
	}
	
	@GetMapping(path = "/marketplace/v1/products/{productId}/includes", produces = "application/json")
	public ResponseEntity<String> productIncludes(@PathVariable("productId") String productId) {
		String requestStr = String.format("/marketplace/v1/products/%s/includes", productId);
		ApiHelpers result = processGet(requestStr);
		return ResponseEntity.status(result.getStatusCode()).body(result.getResponse());
	}
	
	@GetMapping(path = "/marketplace/v1/products/{productId}/specs", produces = "application/json")
	public ResponseEntity<String> productSpecs(@PathVariable("productId") String productId) {	
		String requestStr = String.format("/marketplace/v1/products/%s/specs", productId);
		ApiHelpers result = processGet(requestStr);
		return ResponseEntity.status(result.getStatusCode()).body(result.getResponse());
	}
	
	@GetMapping(path = "/marketplace/v1/products/{productId}/recommended", produces = "application/json")
	public ResponseEntity<String> productRecommend(@PathVariable("productId") String productId) {
		String requestStr = String.format("/marketplace/v1/products/%s/recommended", productId);
		ApiHelpers result = processGet(requestStr);
		return ResponseEntity.status(result.getStatusCode()).body(result.getResponse());
	}
	
	@GetMapping(path = "/marketplace/v1/products/{productId}/faq", produces = "application/json")
	public ResponseEntity<String> productFaq(@PathVariable("productId") String productId) {
		String requestStr = String.format("/marketplace/v1/products/%s/faq", productId);
		ApiHelpers result = processGet(requestStr);
		return ResponseEntity.status(result.getStatusCode()).body(result.getResponse());
	}
	
	@GetMapping(path = "/marketplace/v1/categories/{categoryName}/industries", produces = "application/json")
	public ResponseEntity<String> industry(@PathVariable("categoryName") String categoryName) {
		String requestStr = String.format("/marketplace/v1/categories/%s/industries", categoryName);
		ApiHelpers result = processGet(requestStr);
		return ResponseEntity.status(result.getStatusCode()).body(result.getResponse());
	}
}

package io.onedriveapi.test.demo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.onedriveapi.test.demo.DemoApplication;

@RestController
@RequestMapping("/")
public class TestController {

	@RequestMapping("onedrivetest")
	public void hello(HttpServletRequest request) throws IOException, JSONException {
		
		String code = request.getQueryString();
		code = code.substring(code.lastIndexOf("=") + 1);
		
		DemoApplication.testapi(code);
	}
	
}

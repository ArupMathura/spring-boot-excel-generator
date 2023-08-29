package com.example.demo3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class HelloController {
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello() {
        return "Hello, World!";
    }
	
	@RequestMapping(value = "/world", method = RequestMethod.GET)
    public String world() {
        return "Hello, Love!";
    }
	
	@RequestMapping(value = "/post-example", method = RequestMethod.POST)
    public String postExample() {
        return "This is a POST request example!";
    }
	
	@RequestMapping(value = "/put-example", method = RequestMethod.PUT)
	public String putExample() {
		return "this is a put request example";
	}
	
	@RequestMapping(value = "/delete-example", method = RequestMethod.DELETE)
	public String deleteExample() {
		return "this is a delete request example";
	}
}

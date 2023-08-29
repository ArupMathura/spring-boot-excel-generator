package com.example.demo3.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;
@RestController
public class TestController {
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String test() {
        return "Hello, Love!";
    }
}

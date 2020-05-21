package com.laoniu.controller;

import com.laoniu.annotation.LaoNiuRequestMapping;

@LaoNiuRequestMapping("/test")
public class TestController {

	@LaoNiuRequestMapping("test1")
	public String test1() {
		
		return "test1";
	}
	@LaoNiuRequestMapping("test2")
	public String test2() {
		
		return "test2";
	}
	@LaoNiuRequestMapping("test3")
	public String test3() {
		
		return "test3";
	}
}

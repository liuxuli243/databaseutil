package com.laoniu.controller;

import com.laoniu.annotation.LaoNiuRequestMapping;
import com.laoniu.annotation.LaoNiuResponseBody;

@LaoNiuRequestMapping("/test")
public class TestController {

	@LaoNiuRequestMapping(value = "test1", description = "测试方法1")
	@LaoNiuResponseBody
	public String test1() {
		return "test1";
	}
	@LaoNiuRequestMapping(value = "test2", description = "测试方法2")
	@LaoNiuResponseBody
	public String test2() {
		
		return "test2";
	}
	@LaoNiuRequestMapping(value = "test3", description = "测试方法3")
	@LaoNiuResponseBody
	public String test3() {
		
		return "test3";
	}
}

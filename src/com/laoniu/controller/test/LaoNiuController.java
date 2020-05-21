package com.laoniu.controller.test;

import com.laoniu.annotation.LaoNiuRequestMapping;

public class LaoNiuController {

	@LaoNiuRequestMapping("laoniutest")
	public String test() {
		return "老牛测试的类";
	}
}

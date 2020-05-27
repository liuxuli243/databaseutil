package com.laoniu.controller.test;

import com.laoniu.annotation.LaoNiuRequestMapping;
import com.laoniu.annotation.LaoNiuResponseBody;

public class LaoNiuController {

	@LaoNiuRequestMapping(value = "laoniutest",description = "测试方法")
	@LaoNiuResponseBody
	public String test() {
		return "老牛测试的类";
	}
}

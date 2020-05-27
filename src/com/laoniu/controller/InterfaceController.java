package com.laoniu.controller;

import javax.servlet.http.HttpServletRequest;

import com.laoniu.annotation.LaoNiuRequestMapping;
import com.laoniu.utils.UrlMapping;
@LaoNiuRequestMapping("interface")
public class InterfaceController {

	@LaoNiuRequestMapping(value =  "methodlist", description = "方法列表")
	public String methodlist(HttpServletRequest request) {
		request.setAttribute("methodlist", UrlMapping.urlmap.values());
		return "methodlist";
	}
}

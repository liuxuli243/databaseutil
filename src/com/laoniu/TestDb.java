package com.laoniu;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laoniu.annotation.LaoNiuParam;
import com.laoniu.annotation.LaoNiuResponseBody;
import com.laoniu.binding.RequestMethod;
import com.laoniu.utils.ObjectUtils;
import com.laoniu.utils.UrlMapping;
/**
 * 拦截所有.db为结尾的请求
　 * Title: TestDb
　 * Description: 
　 * @author 刘旭利
　 * @date 2020年5月21日
 */
public class TestDb extends HttpServlet{

	private String prefix;
	
	private String suffix;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//分发请求
		dispatcher(request, response);
	}
	
	@Override
	public void init() throws ServletException {
		UrlMapping.init();
		this.prefix = this.getInitParameter("prefix");
		this.suffix = this.getInitParameter("suffix");
		super.init();
	}
	
	/**
	 * 分发请求
	*Title: dispatcher
	*author:刘旭利
	*Description:
	 */
	private void dispatcher(HttpServletRequest request, HttpServletResponse response) {
		String url = request.getRequestURI();
		
		RequestMethod requestMethod = UrlMapping.urlmap.get(url);
		try {
			Parameter[] parameters = requestMethod.getMethod().getParameters();
			
			Enumeration<String> parameterNames = request.getParameterNames();
			Object[] params = new Object[requestMethod.getMethod().getParameterCount()]; 
			for (int i = 0; i < parameters.length; i++) {
				//如果参数是request
				if(parameters[i].getType().getName().equals(HttpServletRequest.class.getName())) {
					params[i] = request;
				}else if(parameters[i].getType().getName().equals(HttpServletResponse.class.getName())) {
					//如果参数是respose
					params[i] = response;
				}else {
					//如果是基本数据类型，直接封装
					if (ObjectUtils.isBaseType(parameters[i].getType(), true)) {
						if (parameters[i].isAnnotationPresent(LaoNiuParam.class)) {
							String name = parameters[i].getAnnotation(LaoNiuParam.class).value();
							params[i] = ObjectUtils.convert(request.getParameter(name),parameters[i].getType());
						}else {
							params[i] = null;
						}
					}else {
						Object paramobj = parameters[i].getType().newInstance();
						while (parameterNames.hasMoreElements()) {
							String paramstr = parameterNames.nextElement();
							Field[] declaredFields = parameters[i].getType().getDeclaredFields();
							for (Field field : declaredFields) {
								if (field.getName().equals(paramstr)) {
									field.setAccessible(true);
									field.set(paramobj, request.getParameter(paramstr));
								}
							}
						}
						params[i] = paramobj;
					}
				}
			}
			Object invoke = requestMethod.getMethod().invoke(requestMethod.getInstance(), params);
			//如果返回空，将响应结果返回到页面
			if (requestMethod.getMethod().isAnnotationPresent(LaoNiuResponseBody.class)) {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json");
				response.getWriter().print(JSON.toJSONString(invoke));
				return;
			}
			//将返回结果解析成页面
			if (requestMethod.getMethod().getReturnType().getName().equals(String.class.getName())) {
				request.getRequestDispatcher(this.prefix + invoke + this.suffix).forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

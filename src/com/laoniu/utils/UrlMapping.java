package com.laoniu.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.laoniu.annotation.LaoNiuRequestMapping;
import com.laoniu.binding.RequestMethod;

public class UrlMapping {

	public static Map<String, RequestMethod> urlmap = new HashMap<String, RequestMethod>();
	
	private static Logger logger = LoggerFactory.getLogger(UrlMapping.class);
	
	public static String packagename = "com.laoniu.controller";
	
	static {
		String path = UrlMapping.class.getResource("/").getPath();
		File file = new File(path + packagename.replace(".", "/"));
		//加载controller类，初始化请求映射
		initUrlMappint(file,path);
	}
	
	public static void init() {
		
	}
	
	
	/**
	 * 初始化一些配置
	*Title: initUrlMappint
	*author:刘旭利
	*Description: 
	　 * @param file
	　 * @param path
	 */
	private static void initUrlMappint(File file,String path) {
		
		File[] listFiles = file.listFiles();
		for (File controllerfile : listFiles) {
			if (controllerfile.isFile()) {
				try {
					String fileclassname = controllerfile.getPath().substring(path.length()-1, controllerfile.getPath().length());
					if (fileclassname.startsWith("/")) {
						fileclassname = fileclassname.substring(1);
					}
					fileclassname = fileclassname.replace(".class", "").replace("\\", ".");
					Class<?> controllerclazz = Class.forName(fileclassname);
					String url = "";
					String baseurl = "";
					if (controllerclazz.isAnnotationPresent(LaoNiuRequestMapping.class)) {
						String value = controllerclazz.getAnnotation(LaoNiuRequestMapping.class).value();
						if (value.startsWith("/")) {
							url += value;
						}else {
							url += "/" + value;
						}
					}
					baseurl = url;
					//创建controller对象
					Object newInstance = controllerclazz.newInstance();
					Method[] methods = controllerclazz.getMethods();
					for (Method method : methods) {
						if (method.isAnnotationPresent(LaoNiuRequestMapping.class)) {
							String value = method.getAnnotation(LaoNiuRequestMapping.class).value();
							if (value.startsWith("/")) {
								url += value;
							}else {
								url += "/" + value;
							}
							String description = method.getAnnotation(LaoNiuRequestMapping.class).description();
							RequestMethod requestMethod = new RequestMethod();
							requestMethod.setInstance(newInstance);
							requestMethod.setMethod(method);
							requestMethod.setDescription(description);
							requestMethod.setUrl(url);
							logger.info("注册地址："+url+"，类名："+controllerclazz.getName() + "，方法："+method.getName());
							urlmap.put(url, requestMethod);
							url = baseurl;
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}else if (controllerfile.isDirectory()) {
				//如果是目录，则继续扫描子包
				initUrlMappint(controllerfile,path);
			}
		}
	}
}

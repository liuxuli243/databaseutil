package com.laoniu.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.laoniu.utils.UrlMapping;

public class LaoNiuListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		/**
		 * 项目启动的时候调用UrlMapping的方法，目的是加载urlmapping类，使其执行类中的static代码块，初始化一些配置
		 */
		UrlMapping.init();
	}

}

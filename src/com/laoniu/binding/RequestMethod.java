package com.laoniu.binding;

import java.lang.reflect.Method;

public class RequestMethod {

	private Object instance;
	
	private Method method;

	public Object getInstance() {
		return instance;
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
	
	
}

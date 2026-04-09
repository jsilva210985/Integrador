package com.integrador.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext ac) {
		context = ac;
	}

	public static Object getBean(Class<?> clazz) {
		return context.getBean(clazz);
	}

	public static Object getBean(String className) throws Exception {
		Class<?> clazz = Class.forName(className);
		return context.getBean(clazz);
	}
}
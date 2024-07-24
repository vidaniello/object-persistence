package com.github.vidaniello.objectpersistence.enanchment;

import java.lang.reflect.Method;
import java.util.Map;

import com.github.vidaniello.objectpersistence.PersistentReference;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;

public class DynamicPersistentClassMethodHandler<T> implements MethodHandler {

	private ClassConfiguration<T> classConfiguration;
	private Map<Method,PersistentReference> references;
	
	private DynamicPersistentClassMethodHandler() {
		
	}
	
	DynamicPersistentClassMethodHandler(ClassConfiguration<T> classConfiguration) {
		this.classConfiguration = classConfiguration;
	}
		
	@Override
	public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
		
		//Before
		Object fromMethod = proceed.invoke(self, args);
		//After
		
		return fromMethod;
	}
	
	
	


}

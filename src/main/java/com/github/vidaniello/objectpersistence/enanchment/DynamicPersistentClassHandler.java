package com.github.vidaniello.objectpersistence.enanchment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;

public class DynamicPersistentClassHandler<T> implements MethodHandler, MethodFilter {

	private Class<T> enanchedClass;
	private List<Method> handledsMethods;
	
	public DynamicPersistentClassHandler() {
		
	}
	
	public DynamicPersistentClassHandler(Class<T> enanchedClass) {
		this.enanchedClass = enanchedClass;
	}
	
	public List<Method> getHandledsMethods() {
		if(handledsMethods==null)
			handledsMethods = new ArrayList<>();
		return handledsMethods;
	}
	
	public Class<T> getEnanchedClass() {
		return enanchedClass;
	}
	
	@Override
	public boolean isHandled(Method m) {
		
		return false;
	}

	@Override
	public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
		
		return null;
	}

}

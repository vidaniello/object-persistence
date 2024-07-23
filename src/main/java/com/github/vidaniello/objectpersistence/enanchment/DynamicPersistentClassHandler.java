package com.github.vidaniello.objectpersistence.enanchment;

import java.lang.reflect.Method;
import java.util.Map;

import com.github.vidaniello.objectpersistence.PersistentReference;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;

public class DynamicPersistentClassHandler<T> implements MethodHandler, MethodFilter {

	private ClassConfiguration<T> classConfiguration;
	private Map<Method,PersistentReference> references;
	
	private DynamicPersistentClassHandler() {
		
	}
	
	DynamicPersistentClassHandler(ClassConfiguration<T> classConfiguration) {
		this.classConfiguration = classConfiguration;
	}
		
	@Override
	public boolean isHandled(Method m) {
		
		
		//if(ret) {
			//check annotated field
		//}
		
		return true;
	}

	@Override
	public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
		
		return null;
	}
	
	
	


}

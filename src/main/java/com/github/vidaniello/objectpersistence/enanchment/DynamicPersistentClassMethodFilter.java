package com.github.vidaniello.objectpersistence.enanchment;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.ProxyFactory;

public class DynamicPersistentClassMethodFilter<T> implements MethodFilter {

	private ProxyFactory proxyFactory;
	private ClassConfiguration<T> classConfiguration;
	
	private DynamicPersistentClassMethodFilter() {
		
	}
	
	DynamicPersistentClassMethodFilter(ClassConfiguration<T> classConfiguration, ProxyFactory proxyFactory) {
		this.classConfiguration = classConfiguration;
		this.proxyFactory = proxyFactory;
	}
	
	@Override
	public boolean isHandled(Method m) {
		if(m.getDeclaringClass().equals(classConfiguration.getOriginalClass()))
			return classConfiguration.getManagedFieldEntities().stream().filter(efc->m.equals(efc.getGetterMethod())||m.equals(efc.getSetterMethod())).findAny().orElse(null)!=null;
		return false;
	}

}

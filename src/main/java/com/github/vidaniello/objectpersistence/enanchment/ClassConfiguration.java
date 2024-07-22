package com.github.vidaniello.objectpersistence.enanchment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassConfiguration<T> {
	
	private Class<T> enanchedClass;
	private List<Method> managedMethods;
	
	public ClassConfiguration() {
		
	}
	
	public ClassConfiguration(Class<T> enanchedClass) {
		this.enanchedClass = enanchedClass;
	}

	public Class<T> getEnanchedClass() {
		return enanchedClass;
	}
	
	public List<Method> getManagedMethods() {
		if(managedMethods==null)
			managedMethods  = new ArrayList<>();
		return managedMethods;
	}
	
	
}

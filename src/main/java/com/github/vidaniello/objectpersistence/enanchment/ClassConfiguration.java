package com.github.vidaniello.objectpersistence.enanchment;

import java.util.ArrayList;
import java.util.List;

public class ClassConfiguration<T> {
	
	private Class<T> originalClass;
	private Class<T> enanchedClass;
	private boolean hasPersistentFields;
	private List<EntityFieldConfiguration> managedFieldEntities;
	
	public ClassConfiguration() {
		
	}
	
	public ClassConfiguration(Class<T> originalClass) {
		this.originalClass = originalClass;
	}

	public Class<T> getOriginalClass() {
		return originalClass;
	}
	
	public Class<T> getEnanchedClass() {
		return enanchedClass;
	}
	
	public void setEnanchedClass(Class<T> enanchedClass) {
		this.enanchedClass = enanchedClass;
	}
	
	public List<EntityFieldConfiguration> getManagedFieldEntities() {
		if(managedFieldEntities==null)
			managedFieldEntities  = new ArrayList<>();
		return managedFieldEntities;
	}
	
	public boolean hasPersistentFields() {
		return hasPersistentFields;
	}
	
	public void setHasPersistentFields(boolean hasPersistentFields) {
		this.hasPersistentFields = hasPersistentFields;
	}
}

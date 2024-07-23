package com.github.vidaniello.objectpersistence.enanchment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.github.vidaniello.objectpersistence.PersistentRepositoryConfig;

public class EntityFieldConfiguration {

	private ClassConfiguration<?> parentClass;
	private PersistentRepositoryConfig persistentRepositoryConfig;
	private Field field;
	private Method getterMethod;
	private Method setterMethod;
	
	public EntityFieldConfiguration() {
		
	}

	public EntityFieldConfiguration(ClassConfiguration<?> parentClass) {
		this.parentClass = parentClass;
	}

	public PersistentRepositoryConfig getPersistentRepositoryConfig() {
		return persistentRepositoryConfig;
	}

	public void setPersistentRepositoryConfig(PersistentRepositoryConfig persistentRepositoryConfig) {
		this.persistentRepositoryConfig = persistentRepositoryConfig;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Method getGetterMethod() {
		return getterMethod;
	}

	public void setGetterMethod(Method getterMethod) {
		this.getterMethod = getterMethod;
	}

	public Method getSetterMethod() {
		return setterMethod;
	}

	public void setSetterMethod(Method setterMethod) {
		this.setterMethod = setterMethod;
	}

	public ClassConfiguration<?> getParentClass() {
		return parentClass;
	}
	
	
	
}

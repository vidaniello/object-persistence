package com.github.vidaniello.objectpersistence.enanchment;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.github.vidaniello.objectpersistence.PersistenceReferenceFactory;
import com.github.vidaniello.objectpersistence.PersistentObjectReference;

import javassist.util.proxy.MethodHandler;

public class DynamicPersistentClassMethodHandler<T> implements MethodHandler {

	private ClassConfiguration<T> classConfiguration;
	private Map<Method,EntityFieldWrapper> references = new HashMap<>();
	
	private DynamicPersistentClassMethodHandler() {
		
	}
	
	DynamicPersistentClassMethodHandler(ClassConfiguration<T> classConfiguration) {
		this.classConfiguration = classConfiguration;
		for(EntityFieldConfiguration efc : classConfiguration.getManagedFieldEntities()) {
			EntityFieldWrapper efw = new EntityFieldWrapper(efc);
			
			if(efc.getGetterMethod()!=null) references.put(efc.getGetterMethod(), efw);
			if(efc.getSetterMethod()!=null) references.put(efc.getSetterMethod(), efw);
		}
	}
		
	@Override
	public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
		
		synchronized (references.get(thisMethod)) {
			EntityFieldWrapper efw = references.get(thisMethod);
			
			Method getter = efw.getEntityFieldConfiguration().getGetterMethod();
			Method setter = efw.getEntityFieldConfiguration().getSetterMethod();
			
			//initialization of persistent reference
			if(efw.getPersistentReference()==null) initPersistentReference(efw, self);
			
			if(thisMethod.equals(efw.getEntityFieldConfiguration().getGetterMethod()))
				loadField(efw, self);
			
			//Before
			Object fromMethod = proceed.invoke(self, args);
			//After
			
			if(thisMethod.equals(efw.getEntityFieldConfiguration().getSetterMethod()))
				saveField(efw, self);
			
			return fromMethod;
		}
		
	}
	
	private void initPersistentReference(EntityFieldWrapper efw, Object self) throws Exception {
		
		Class<?> entityClass = efw.getEntityFieldConfiguration().getField().getType();
		
		if(entityClass.isAssignableFrom(Iterable.class) || entityClass.isAssignableFrom(Map.class)) {
			
			//Persistent collection;
			
			//Collection
			//List
			//Set
			//Map
			
			
		} else {
			
			//Persistent object reference
			PersistentObjectReference<?> por = PersistenceReferenceFactory.getReference(efw.getEntityFieldConfiguration().getField(), self);
			efw.setPersistentReference(por);
			
		}
		
	}
	
	
	private void loadField(EntityFieldWrapper efw, Object self) throws IllegalArgumentException, IllegalAccessException, Exception {
		if(efw.getPersistentReference().getClass().equals(PersistentObjectReference.class)) {
			PersistentObjectReference<?> por = (PersistentObjectReference<?>) efw.getPersistentReference();
			efw.getEntityFieldConfiguration().getField().set(self, por.getValue());
		}
	}

	private void saveField(EntityFieldWrapper efw, Object self) throws IllegalArgumentException, IllegalAccessException, Exception {
		if(efw.getPersistentReference().getClass().equals(PersistentObjectReference.class)) {
			@SuppressWarnings("unchecked")
			PersistentObjectReference<Object> por = (PersistentObjectReference<Object>) efw.getPersistentReference();
			Object objFromField = efw.getEntityFieldConfiguration().getField().get(self);
			por.setValue(objFromField);
		}
	}

}

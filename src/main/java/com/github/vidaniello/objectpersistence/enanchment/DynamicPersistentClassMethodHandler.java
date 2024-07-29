package com.github.vidaniello.objectpersistence.enanchment;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.vidaniello.objectpersistence.PersistenceReferenceFactory;
import com.github.vidaniello.objectpersistence.PersistentList;
import com.github.vidaniello.objectpersistence.PersistentObjectReference;
import com.github.vidaniello.objectpersistence.PersistentReference;

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
			if(efw.getPersistentReference()==null) { 
				Object preInstancedField = proceed.invoke(self, args);
				initPersistentReference(efw, self, preInstancedField); 
			}
			
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
	
	private void initPersistentReference(EntityFieldWrapper efw, Object self, Object preInstancedField) throws Exception {
		
		Class<?> entityClass = efw.getEntityFieldConfiguration().getField().getType();
		
		PersistentReference pr = null;
		
		if(Iterable.class.isAssignableFrom(entityClass) || Map.class.isAssignableFrom(entityClass)) {			
			
			
			if(List.class.isAssignableFrom(entityClass)) {
				//List
				pr = PersistenceReferenceFactory.getListReference(efw.getEntityFieldConfiguration().getField(), self, preInstancedField);
			} else if(Set.class.isAssignableFrom(entityClass)) {
				//Set
				
			} else if(Collection.class.isAssignableFrom(entityClass)) {
				//Collection
				
			} else if(Map.class.isAssignableFrom(entityClass)) {
				//Map
				
			} else
				throw new Exception("Iterable or Map interface of '"+entityClass.getCanonicalName()+"' is not managed");
			
			
		} else {
			
			//Persistent object reference
			pr = PersistenceReferenceFactory.getReference(efw.getEntityFieldConfiguration().getField(), self);			
		}
		
		efw.setPersistentReference(pr);
	}
	
	
	private void loadField(EntityFieldWrapper efw, Object self) throws IllegalArgumentException, IllegalAccessException, Exception {
		if(PersistentObjectReference.class.isAssignableFrom(efw.getPersistentReference().getClass())) {
			PersistentObjectReference<?> por = (PersistentObjectReference<?>) efw.getPersistentReference();
			efw.getEntityFieldConfiguration().getField().set(self, por.getValue());
		}
	}

	private void saveField(EntityFieldWrapper efw, Object self) throws IllegalArgumentException, IllegalAccessException, Exception {
		if(PersistentObjectReference.class.isAssignableFrom(efw.getPersistentReference().getClass())) {
			@SuppressWarnings("unchecked")
			PersistentObjectReference<Object> por = (PersistentObjectReference<Object>) efw.getPersistentReference();
			Object objFromField = efw.getEntityFieldConfiguration().getField().get(self);
			por.setValue(objFromField);
		}
	}

}

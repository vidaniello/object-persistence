package com.github.vidaniello.objectpersistence.enanchment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.vidaniello.objectpersistence.PersistenceReferenceFactory;
import com.github.vidaniello.objectpersistence.PersistentCollectionIterable;
import com.github.vidaniello.objectpersistence.PersistentMapIterable;
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
			
			//Method getter = efw.getEntityFieldConfiguration().getGetterMethod();
			//Method setter = efw.getEntityFieldConfiguration().getSetterMethod();
			
			//Object fromMethod = proceed.invoke(self, args);
			//Object fromMethod = null;
			
			//initialization of persistent reference
			if(efw.getPersistentReference()==null) 
				initPersistentReference(efw, self, proceed, args); 
			
			if(thisMethod.equals(efw.getEntityFieldConfiguration().getGetterMethod()))
				return getField(efw, self, proceed.invoke(self, args));
			else if(thisMethod.equals(efw.getEntityFieldConfiguration().getSetterMethod())) {
				Object _arg = setField(efw, self, args[0]);
				return proceed.invoke(self, _arg);
			}
			return proceed.invoke(self, args);
			//return fromMethod;
		}
		
	}
	
	private void initPersistentReference(EntityFieldWrapper efw, Object self, Method proceed, Object[] args) throws Exception {
		
		Class<?> entityClass = efw.getEntityFieldConfiguration().getField().getType();
		
		PersistentReference pr = null;
		
		if(Iterable.class.isAssignableFrom(entityClass) || Map.class.isAssignableFrom(entityClass)) {			
			
			//Object fromMethod = proceed.invoke(self, args);
			
			if(List.class.isAssignableFrom(entityClass)) {
				//List
				pr = PersistenceReferenceFactory.getListReference(efw.getEntityFieldConfiguration().getField(), self/*, fromMethod*/);
			} else if(Set.class.isAssignableFrom(entityClass)) {
				//Set
				
			} else if(Collection.class.isAssignableFrom(entityClass)) {
				//Collection
				
			} else if(Map.class.isAssignableFrom(entityClass)) {
				//Map
				
			} else
				throw new Exception("Iterable or Map interface of type '"+entityClass.getCanonicalName()+"' is not managed");
			
			
			
			
		} else {
			
			//Persistent object reference
			pr = PersistenceReferenceFactory.getReference(efw.getEntityFieldConfiguration().getField(), self);			
		}
		
		efw.setPersistentReference(pr);
	}
	
	private boolean collectionInitialized;
	
	@SuppressWarnings("unchecked")
	private Object getField(EntityFieldWrapper efw, Object self, Object invokeFromMethod) throws IllegalArgumentException, IllegalAccessException, Exception {
		
		Object toret = invokeFromMethod;
		
		if(PersistentObjectReference.class.isAssignableFrom(efw.getPersistentReference().getClass())) {
			
			PersistentObjectReference<?> por = (PersistentObjectReference<?>) efw.getPersistentReference();
			Object valueFromPersistence = por.getValue();
			
			if(valueFromPersistence!=null /*&& fromMethod==null*/) {
				efw.getEntityFieldConfiguration().getField().set(self, valueFromPersistence);
				toret = valueFromPersistence;
			}
		
		} else {
			
			Object fromField = efw.getEntityFieldConfiguration().getField().get(self);
			
			if(efw.getPersistentReference()!=fromField) {
				
				//First inizialization
				
			} else 
				return invokeFromMethod;
			
			if(collectionInitialized)
				return invokeFromMethod;
			
			collectionInitialized = true;
			
			if(PersistentCollectionIterable.class.isAssignableFrom(efw.getPersistentReference().getClass())) {
				
				/*
				Collection<?> fromMeth = new ArrayList<>();
				
				if(fromMethod!=null)
					if(Collection.class.isAssignableFrom(fromMethod.getClass()))
						fromMeth = (Collection<?>) fromMethod;
				*/		
						
				PersistentCollectionIterable<?,?> pci =  (PersistentCollectionIterable<?,?>) efw.getPersistentReference();
				@SuppressWarnings("rawtypes")
				Collection wrappedCollection = (Collection) pci.getCollection();
				
				/*
				if(wrappedCollection.isEmpty() && !fromMeth.isEmpty())
					fromMeth.forEach(wrappedCollection::add);
				*/
				
				/*
				if(!wrappedCollection.isEmpty()) {
					efw.getEntityFieldConfiguration().getField().set(self, wrappedCollection);
					toret = wrappedCollection;
				}
				*/
				if(wrappedCollection!=null) {
					efw.getEntityFieldConfiguration().getField().set(self, pci);
					toret = pci;
				} else if(invokeFromMethod!=null) {
					
					if(Collection.class.isAssignableFrom(invokeFromMethod.getClass())) {
					
						Collection<?> coll = (Collection<?>) invokeFromMethod;
						pci.setCollectionNewInstance((Class<? extends Collection<?>>) coll.getClass());
						
						//Collection _wrappedCollection = pci.getCollection();
						@SuppressWarnings("rawtypes")
						Collection persistColl = (Collection) pci;
						
						coll.forEach(persistColl::add);
						efw.getEntityFieldConfiguration().getField().set(self, pci);
						toret = pci;
					}
						
				} else {
					efw.getEntityFieldConfiguration().getField().set(self, null);
					toret = null;
				}
				
				
			} else if(PersistentMapIterable.class.isAssignableFrom(efw.getPersistentReference().getClass())) {
				
			}
			
		}
		return toret;
	}

	@SuppressWarnings("unchecked")
	private Object setField(EntityFieldWrapper efw, Object self, Object arg) throws IllegalArgumentException, IllegalAccessException, Exception {
		
		Object objFromField = efw.getEntityFieldConfiguration().getField().get(self);
		
		//Check same instance
		if(objFromField==arg)
			return arg;
		
		if(PersistentObjectReference.class.isAssignableFrom(efw.getPersistentReference().getClass())) {
			
			PersistentObjectReference<Object> por = (PersistentObjectReference<Object>) efw.getPersistentReference();
			por.setValue(arg);
			return arg;
			
		} else {
			
			if(PersistentCollectionIterable.class.isAssignableFrom(efw.getPersistentReference().getClass())) {
				
				/*
				Collection<?> fromMeth = new ArrayList<>();
				
				if(arg!=null)
					if(Collection.class.isAssignableFrom(arg.getClass()))
						fromMeth = (Collection<?>) arg;
				*/
				
				PersistentCollectionIterable<?,?> pci =  (PersistentCollectionIterable<?,?>) efw.getPersistentReference();
				@SuppressWarnings("rawtypes")
				Collection wrappedCollection = pci.getCollection();
				
				if(wrappedCollection!=null)
					wrappedCollection.clear();
				
				if(arg!=null) {
					
					if(Collection.class.isAssignableFrom(arg.getClass())) {
						Collection<?> coll = (Collection<?>) arg;
						pci.setCollectionNewInstance((Class<? extends Collection<?>>) coll.getClass());
						@SuppressWarnings("rawtypes")
						Collection _wrappedCollection = pci.getCollection();
						coll.forEach(_wrappedCollection::add);
						return _wrappedCollection;
					}
					
				} else {
					pci.setCollection(null);
					collectionInitialized = false;
				}
							
				//fromMeth.forEach(wrappedCollection::add);
				
			} else if(PersistentMapIterable.class.isAssignableFrom(efw.getPersistentReference().getClass())) {
				
			}
				
		}
		
		return arg;
	}

}

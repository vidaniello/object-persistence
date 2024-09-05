package com.github.vidaniello.objectpersistence.enanchment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.vidaniello.objectpersistence.PersistenceIterable;
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
		
		EntityFieldWrapper efw = references.get(thisMethod);
		
		synchronized (efw) {
			
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
		
		if(Iterable.class.isAssignableFrom(entityClass) ) {			
			
			//Persistent Iterable reference
			
			if(List.class.isAssignableFrom(entityClass)) {
				//List
				pr = PersistenceReferenceFactory.getListReference(efw.getEntityFieldConfiguration().getField(), self);
			} else if(Set.class.isAssignableFrom(entityClass)) {
				//Set
				pr = PersistenceReferenceFactory.getSetReference(efw.getEntityFieldConfiguration().getField(), self);
			} else if(Collection.class.isAssignableFrom(entityClass)) {
				//Collection
				pr = PersistenceReferenceFactory.getCollectionReference(efw.getEntityFieldConfiguration().getField(), self);
			} else
				throw new Exception("Iterable or Map interface of type '"+entityClass.getCanonicalName()+"' is not managed");
			
			//Put in the field the reference
			efw.getEntityFieldConfiguration().getField().set(self, pr);
			
		} else if(Map.class.isAssignableFrom(entityClass)) {
			
			//Map
			pr = PersistenceReferenceFactory.getMapReference(efw.getEntityFieldConfiguration().getField(), self);
			
		} else {
			
			//Persistent Object reference
			pr = PersistenceReferenceFactory.getReference(efw.getEntityFieldConfiguration().getField(), self);			
		}
		
		efw.setPersistentReference(pr);
	}
	
	private boolean routineGet;
	
	private Object getField(EntityFieldWrapper efw, Object self, Object invokeFromMethod) throws IllegalArgumentException, IllegalAccessException, Exception {
		
		Object toret = invokeFromMethod;
		
		if(PersistentObjectReference.class.isAssignableFrom(efw.getPersistentReference().getClass())) {
			
			PersistentObjectReference<?> por = (PersistentObjectReference<?>) efw.getPersistentReference();
			Object valueFromPersistence = por.getValue();
			
			if(valueFromPersistence!=null) {
				efw.getEntityFieldConfiguration().getField().set(self, valueFromPersistence);
				toret = valueFromPersistence;
			}
		
		} else {
			
			toret = efw.getEntityFieldConfiguration().getField().get(self);
			
			if(!routineGet){
				
				routineGet = true;
				
				PersistenceIterable pi = (PersistenceIterable) efw.getPersistentReference();
				Object wrappedCollection = pi.getWrappedIterable();
				
				if(wrappedCollection==null){
					
					if(invokeFromMethod!=null)
						newInstanceAndLoading(pi, invokeFromMethod);
					else 
						PersistenceReferenceFactory.newDefaultInstance(pi);

				}
			}
		}
		
		return toret;
	}	
	
	@SuppressWarnings("unchecked")
	private Object setField(EntityFieldWrapper efw, Object self, Object arg) throws IllegalArgumentException, IllegalAccessException, Exception {
		
		if(PersistentObjectReference.class.isAssignableFrom(efw.getPersistentReference().getClass())) {
			
			PersistentObjectReference<Object> por = (PersistentObjectReference<Object>) efw.getPersistentReference();
			por.setValue(arg);
			return arg;
			
		} else {
			
			Object objFromField = efw.getEntityFieldConfiguration().getField().get(self);
			
			//Check same instance
			if(objFromField==arg)
				return arg;
			
			PersistenceIterable pi = (PersistenceIterable) efw.getPersistentReference();
						
			if(pi.getWrappedIterable()!=null)
				pi.clearIterable();
			
			if(arg==null) 				
				routineGet = false;
			else 				
				newInstanceAndLoading(pi, arg);
			
			return pi;
			
		}
	}
	
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void newInstanceAndLoading(PersistenceIterable pi, Object iterable) throws Exception {
		
		if(iterable==null)
			PersistenceReferenceFactory.newDefaultInstance(pi);
		 else if(PersistentCollectionIterable.class.isAssignableFrom(pi.getClass())) {
			
			 //Collection
			
			PersistentCollectionIterable pci = (PersistentCollectionIterable) pi;			
			pci.setCollectionNewInstance(iterable.getClass());
			
			Collection itCasted = (Collection) iterable;
			Collection pciCasted = (Collection) pci;
			
			itCasted.stream().forEach(pciCasted::add);
			
		} else if(PersistentMapIterable.class.isAssignableFrom(pi.getClass())) {
			
			//Map
			
			PersistentMapIterable pmi = (PersistentMapIterable) pi;
			pmi.setMapNewInstance(iterable.getClass());
			
			Map itCasted = (Map) iterable;
			Map pmiCasted = (Map) pmi;
			
			itCasted.forEach(pmiCasted::put);
			
		} else
			throw new Exception("Iterable or Map interface of type '"+pi.getClass().getCanonicalName()+"' is not managed");
	}
	
	
}

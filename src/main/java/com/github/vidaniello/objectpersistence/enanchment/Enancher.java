package com.github.vidaniello.objectpersistence.enanchment;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.github.vidaniello.objectpersistence.PersistentRepositoryConfig;

import javassist.util.proxy.ProxyFactory;

public class Enancher {
	
	public static <T> T getNewProxyInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
		
		ClassConfiguration<T> cfg = getEnanchedClassConfiguration(clazz);
		
		if(cfg!=null) {
			
			DynamicPersistentClassMethodHandler<T> hnd = new DynamicPersistentClassMethodHandler<>(cfg);
			
			ProxyFactory pf = EnancherCache.get().getProxyFactory(clazz, cfg);
			
			@SuppressWarnings("unchecked")
			T newInstance = (T) pf.create(new Class[0], new Object[0], hnd);
			
			return newInstance;
			
		}
		
		return clazz.newInstance();
	}
	
	public static synchronized boolean isClassEnanched(Class<?> clazz) {
		return getEnanchedClassConfiguration(clazz)!=null;
	}
	
	public static synchronized <T> ClassConfiguration<T> getEnanchedClassConfiguration(Class<T> clazz) {
		
		@SuppressWarnings("unchecked")
		ClassConfiguration<T> cfg = (ClassConfiguration<T>) EnancherCache.get().getCache().get(clazz);
		
		if(cfg==null) {
			cfg = scanClass(clazz);
			EnancherCache.get().getCache().put(clazz, cfg);
		}

		if(cfg.hasPersistentFields())
			return cfg;
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	public static <T> ClassConfiguration<T> scanClass(Class<T> clazz) {
		
		ClassConfiguration<T> clazzConf = new ClassConfiguration<>(clazz);
		
		Field[] allDeclaredFields = clazz.getDeclaredFields();
		
		for(Field field : allDeclaredFields) {
			if(field.isAnnotationPresent(PersistentRepositoryConfig.class)) {
				
				if(!Modifier.isTransient(field.getModifiers())) 
					throw new RuntimeException("Field '"+field.getName()+"' of class '"+clazz.getCanonicalName()+"' must be transient!");
				
				field.setAccessible(true);
				EntityFieldConfiguration efc = new EntityFieldConfiguration(clazzConf);
				efc.setField(field);
				PersistentRepositoryConfig prc = field.getAnnotation(PersistentRepositoryConfig.class);
				efc.setPersistentRepositoryConfig(prc);
				findGetterAndSetterByNamingConvention(field,efc);
				clazzConf.getManagedFieldEntities().add(efc);
			}
		}
		
		if(!clazzConf.getManagedFieldEntities().isEmpty()) 
			clazzConf.setHasPersistentFields(true);
		
		return clazzConf;
	}
	
	
	public static void findGetterAndSetterByNamingConvention(Field field, EntityFieldConfiguration efc) {
		
		String getterName = getGetter(field.getName());
		String isGetterName = getIsGetter(field.getName());
		String setterName = getSetter(field.getName());
		
		Class<?> declaringClass = field.getDeclaringClass();
		Class<?> enityClass = field.getType();
		
		Method getter = null;
		try {
			 getter = declaringClass.getDeclaredMethod(getterName);
		} catch(NoSuchMethodException e) {
			try {
				getter = declaringClass.getDeclaredMethod(isGetterName);
			} catch(NoSuchMethodException e1) {}
		}
		
		if(getter!=null)
			if(getter.getReturnType().equals(enityClass)) {
				getter.setAccessible(true);
				efc.setGetterMethod(getter);
			}
		
		Method setter = null;
		try {
			setter = declaringClass.getDeclaredMethod(setterName, enityClass);
			setter.setAccessible(true);
			efc.setSetterMethod(setter);
		} catch(NoSuchMethodException e) {}
				
	}
	
	public static String fotmatGetterSetterFieldName(String fieldName) {
		if(fieldName!=null) 
			if(!fieldName.isEmpty()){
				
				if(fieldName.length()==1)
					return fieldName.toUpperCase();
				else {
					
					if(Character.isUpperCase(fieldName.charAt(1)))
						return fieldName;
					else
						return fieldName.toUpperCase().charAt(0)+fieldName.substring(1);
					
				}
				
			}
		return fieldName;
	}
	
	public static String getGetter(String fieldName) {
		return "get"+fotmatGetterSetterFieldName(fieldName);
	}
	
	public static String getIsGetter(String fieldName) {
		return "is"+fotmatGetterSetterFieldName(fieldName);
	}
	
	public static String getSetter(String fieldName) {
		return "set"+fotmatGetterSetterFieldName(fieldName);
	}
	
	/**
	 * Return by naming-convention the possible field setter/getter name. <br>
	 * If strict is specified, return null if the naming convenion is not respected.
	 * @param methodName
	 * @param strict
	 * @return
	 */
	public static String geFieldName(String methodName, boolean strictNamingConvention) {
		try {
			if(methodName.startsWith("get") || methodName.startsWith("set") || methodName.startsWith("is")) {
				
				if(methodName.startsWith("get") || methodName.startsWith("set"))
					methodName = methodName.substring(3);
				else if(methodName.startsWith("is"))
					methodName = methodName.substring(2);
				
				if(Character.isUpperCase(methodName.charAt(0)))
					methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
				
			} else if(strictNamingConvention)
				return null;
			
			return methodName;
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			if(strictNamingConvention)
				return null;
			return methodName;
		}
	}
	
	public static Field findField(Method getterSetterMethod) {
		String fieldName = geFieldName(getterSetterMethod.getName(), true);
				
		if(fieldName!=null) 
			try {
				Field toret = getterSetterMethod.getDeclaringClass().getDeclaredField(fieldName);
				toret.setAccessible(true);
				return toret;
			} catch (NoSuchFieldException | SecurityException e) {
				
			}
			
		return null;
	}
	
	public static boolean hasPersistentAnnotation(Field field) {
		PersistentRepositoryConfig prc = field.getAnnotation(PersistentRepositoryConfig.class);
		return prc!=null;
	}
}

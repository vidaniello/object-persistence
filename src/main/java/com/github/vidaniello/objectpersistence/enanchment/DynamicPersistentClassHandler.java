package com.github.vidaniello.objectpersistence.enanchment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;

public class DynamicPersistentClassHandler<T> implements MethodHandler, MethodFilter {

	private Class<T> enanchedClass;
	private ClassConfiguration<T> classConfiguration;
	
	public DynamicPersistentClassHandler() {
		
	}
	
	public DynamicPersistentClassHandler(Class<T> enanchedClass) {
		this.enanchedClass = enanchedClass;
	}
		
	@Override
	public boolean isHandled(Method m) {
		boolean ret = m.getDeclaringClass().equals(enanchedClass);
		
		if(ret) {
			//check annotated field
		}
		
		return ret;
	}

	@Override
	public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
		
		return null;
	}
	
	
	
	public static <T> T enancheClass(Class<T> clazz) {
		return null;
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
		if(getterSetterMethod.isAnnotationPresent(LinkedField.class)) {
			LinkedField lf = getterSetterMethod.getAnnotation(LinkedField.class);
			if(!lf.value().isEmpty())
				fieldName = lf.value();
		}
		
		if(fieldName!=null) 
			try {
				Field toret = getterSetterMethod.getDeclaringClass().getDeclaredField(fieldName);
				toret.setAccessible(true);
				return toret;
			} catch (NoSuchFieldException | SecurityException e) {
				
			}
			
		return null;
	}

}

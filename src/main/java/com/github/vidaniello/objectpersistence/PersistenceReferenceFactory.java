package com.github.vidaniello.objectpersistence;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersistenceReferenceFactory {

	private static Logger log = LogManager.getLogger();
	


	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void newDefaultInstance(PersistenceIterable pi) throws Exception {
		if(PersistentCollectionIterable.class.isAssignableFrom(pi.getClass())) {
			
			PersistentCollectionIterable pci = (PersistentCollectionIterable) pi;
			
			//Collections
			
			if(PersistentList.class.isAssignableFrom(pi.getClass()))
				pci.setCollectionNewInstance(ArrayList.class);
			else if(PersistentSet.class.isAssignableFrom(pi.getClass()))
				pci.setCollectionNewInstance(HashSet.class);
			else if(PersistentCollection.class.isAssignableFrom(pi.getClass()))
				pci.setCollectionNewInstance(ArrayList.class);
		    else
				throw new Exception("Collectio of type '"+pi.getClass().getCanonicalName()+"' has no default instance configured");
			
		} else if (PersistentMapIterable.class.isAssignableFrom(pi.getClass())) {
			
			//Map
			PersistentMapIterable pmi = (PersistentMapIterable) pi;
			pmi.setMapNewInstance(HashMap.class);
			
		} else
			throw new Exception("Iterable or Map interface of type '"+pi.getClass().getCanonicalName()+"' is not managed");
	}
	
	
	/**
	 * Get a Map reference
	 * @param <KEY>
	 * @param <VALUE>
	 * @param annotatedField
	 * @param dynKeyInst
	 * @return
	 * @throws Exception
	 */
	public static <KEY, VALUE>  PersistentMap<KEY, VALUE> getMapReference(Field annotatedField, Object dynKeyInst) throws Exception{
		
		PersistentMap<KEY, VALUE> ret = null;
			
		try {

			PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(annotatedField, dynKeyInst);
			
			PersistentObjectReference<Map<KEY,PersistentObjectReference<VALUE>>> wrappedReference =	
					new PersistentObjectReferenceImpl<Map<KEY,PersistentObjectReference<VALUE>>>(pori.getCalculatedKey())
				.setPersistentObjectReferenceInfo(pori);
			
			
			ret = new PersistentMapImpl<KEY,VALUE>(wrappedReference);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return ret;
	}
	
	
	/**
	 * Get a Set reference
	 * @param <VALUE>
	 * @param annotatedField
	 * @param dynKeyInst
	 * @return
	 * @throws Exception
	 */
	public static <VALUE>  PersistentSet<VALUE> getSetReference(Field annotatedField, Object dynKeyInst) throws Exception{
		
		PersistentSet<VALUE> ret = null;
				
		try {

			PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(annotatedField, dynKeyInst);
			
			PersistentObjectReference<Set<PersistentObjectReference<VALUE>>> wrappedReference =	
					new PersistentObjectReferenceImpl<Set<PersistentObjectReference<VALUE>>>(pori.getCalculatedKey())
				.setPersistentObjectReferenceInfo(pori);
			
			ret = new PersistentSetImpl<VALUE>(wrappedReference);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return ret;
	}
	
	/**
	 * Get a List reference
	 * @param <VALUE>
	 * @param annotatedField
	 * @param dynKeyInst
	 * @return
	 * @throws Exception
	 */
	public static <VALUE>  PersistentList<VALUE> getListReference(Field annotatedField, Object dynKeyInst) throws Exception{
		
		PersistentList<VALUE> ret = null;
				
		try {

			PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(annotatedField, dynKeyInst);
			
			PersistentObjectReference<List<PersistentObjectReference<VALUE>>> wrappedReference =	
					new PersistentObjectReferenceImpl<List<PersistentObjectReference<VALUE>>>(pori.getCalculatedKey())
				.setPersistentObjectReferenceInfo(pori);
			
			ret = new PersistentListImpl<VALUE>(wrappedReference);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return ret;
	}
	
	
	/**
	 * Get a Collection reference
	 * @param <VALUE>
	 * @param annotatedField
	 * @param dynKeyInst
	 * @return
	 * @throws Exception
	 */
	public static <VALUE> PersistentCollection<VALUE> getCollectionReference(Field annotatedField, Object dynKeyInst) throws Exception{
		
		PersistentCollection<VALUE> ret = null;
				
		try {

			PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(annotatedField, dynKeyInst);
			
			PersistentObjectReference<Collection<PersistentObjectReference<VALUE>>> wrappedReference =	
					new PersistentObjectReferenceImpl<Collection<PersistentObjectReference<VALUE>>>(pori.getCalculatedKey())
				.setPersistentObjectReferenceInfo(pori);
	
			ret = new PersistentCollectionImpl<VALUE>(wrappedReference);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return ret;
	}
	
	/**
	 * Get an Object reference
	 * @param <VALUE>
	 * @param annotatedField
	 * @param dynKeyInst
	 * @return
	 * @throws Exception
	 */
	public static <VALUE> PersistentObjectReference<VALUE> getReference(Field annotatedField, Object dynKeyInst) throws Exception{
		
		PersistentObjectReference<VALUE> ret = null;
		
		try {

			PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(annotatedField, dynKeyInst);
			
			ret = new PersistentObjectReferenceImpl<VALUE>(pori.getCalculatedKey())
						.setPersistentObjectReferenceInfo(pori);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	private static <VALUE> PersistentObjectReferenceInfo getPersistentObjectReferenceInfo(Field annotatedField, Object dynKeyInst) throws Exception{
		PersistentObjectReferenceInfo pori = new PersistentObjectReferenceInfo();
		
		pori.setInstanceForGenerateDynamicKey(dynKeyInst);
		
		Class<?> relationClass = annotatedField.getDeclaringClass();
		
		pori.setRelationClass(relationClass);	
				
		Type genericType = annotatedField.getGenericType();
		
		Class<VALUE> classValue = null;
		if(Class.class.isAssignableFrom(genericType.getClass())) {
			
			//ConcreteClass
			classValue = (Class<VALUE>) genericType;
			
		} else {
			
			//GerericWrapper<ConcreteClass>
			//GerericWrapper<Collection<ConcreteClass>>
			//Collection<ConcreteClass>
			//Collection<GerericWrapper<ConcreteClass>>
			//Map<key,ConcreteClass>
			
			Type[] genRetTypes = ((ParameterizedType)genericType).getActualTypeArguments();
			
			if(Map.class.isAssignableFrom(annotatedField.getType())) {
				
				//Map<key,ConcreteClass>
				//Map<key,GerericWrapper<ConcreteClass>>
				
				//Check the key is not a generic type
				Type typeKey = genRetTypes[0];
				
				//Map<GerericWrapper<ConcreteClass>,ConcreteClass>
				if(!Class.class.isAssignableFrom(typeKey.getClass()))
					throw new IllegalArgumentException("Generic type for a Map key not allowed!");			
				
				Type typeValue = genRetTypes[1];
				
				if(Class.class.isAssignableFrom(typeValue.getClass()))
					//Map<key,ConcreteClass>
					classValue = (Class<VALUE>) typeValue;
				else {
					//Map<key,GerericWrapper<ConcreteClass>>
					ParameterizedType parType = (ParameterizedType) typeValue;
					
					pori.setValueTypeParametrized(true);
					pori.setTypeName(parType.getTypeName());
					Type rawType = parType.getRawType();
					pori.setRawType(rawType);
					
					classValue = (Class<VALUE>) rawType;
				}
				
			} else {
				
				//GerericWrapper<ConcreteClass>
				//GerericWrapper<Collection<ConcreteClass>>
				//Collection<ConcreteClass>
				//Collection<GerericWrapper<ConcreteClass>>
				
				Type type = genRetTypes[0];
				
				if(Class.class.isAssignableFrom(type.getClass()))
					//GerericWrapper<ConcreteClass>
					//Collection<ConcreteClass>
					classValue = (Class<VALUE>) type;
				else {
					//GerericWrapper<Collection<ConcreteClass>>
					//Collection<GerericWrapper<ConcreteClass>>
					ParameterizedType parType = (ParameterizedType) type;
					
					pori.setValueTypeParametrized(true);
					pori.setTypeName(parType.getTypeName());
					Type rawType = parType.getRawType();
					pori.setRawType(rawType);
					
					classValue = (Class<VALUE>) rawType;
				}
				
			}
			
			
		}
			
		
		pori.setValueType(classValue);
		
		
		
		PersistentRepositoryConfig prc = annotatedField.getAnnotation(PersistentRepositoryConfig.class);
		pori.setObjectReferencePersistentRepositoryConfigAnnotation(prc);
		
		String key = "";
		String pKey = prc.primaryKey();
		if(pKey.isEmpty())
			pKey = annotatedField.getName();
		
		pori.setPrimaryKey(pKey);
		key = getDynamicKeyByPattern(pKey, dynKeyInst);
		
		
		pori.setCalculatedKey(key);
		
		return pori;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static final String dynamicKeyPattern = "\\$\\{([a-zA-Z0-9_\\$\\£çèéù€ì]+?\\(\\))\\}|\\$\\{([a-zA-Z0-9_\\$\\£çèéù€ì]+?)\\}";
	public static String getDynamicKeyByPattern(String patt, Object dynamicKeyInstance) throws Exception {
				
		if(patt==null)
			return "";
				
		if(patt.isEmpty())
			return patt;
		
		if(dynamicKeyInstance==null)
			return patt;
				
		Map<String,String> arr = workDynamicKey(patt);
		Map<String,String> results = new HashMap<>();
		
		for(String key : arr.keySet()) {
			String value = arr.get(key);
			if(!value.endsWith("()"))
				results.put(key, getContent( getField(value, dynamicKeyInstance) , dynamicKeyInstance));
			else
				results.put(key, getContent( getMethod(value, dynamicKeyInstance) , dynamicKeyInstance));
		}
		
		for(String key : results.keySet())
			patt = patt.replace(key, results.get(key));
		
		return patt;
	}
	
	public static Map<String,String> workDynamicKey(String text) {
		text = text.replace("${}", "");
		
		Map<String,String> ret = new LinkedHashMap<String, String>();
		
		Pattern patt = Pattern.compile(dynamicKeyPattern);
		Matcher match = patt.matcher(text);
		
		while(match.find())
			ret.put(match.group(), match.group(1)!=null ? match.group(1) : match.group(2) );			
		
		return ret;
	}

	
	private static Field getField(String fieldName, Object dynamicKeyInstance) throws Exception {
		try {
			return dynamicKeyInstance.getClass().getField(fieldName);
		} catch (NoSuchFieldException e) {
			return dynamicKeyInstance.getClass().getDeclaredField(fieldName);
		}
	}
	
	private static Method getMethod(String methodName, Object dynamicKeyInstance) throws Exception {
		try {
			return dynamicKeyInstance.getClass().getMethod( methodName.endsWith("()") ? methodName.replace("()", "") : methodName);
		} catch (NoSuchMethodException e) {
			return dynamicKeyInstance.getClass().getDeclaredMethod( methodName.endsWith("()") ? methodName.replace("()", "") : methodName);
		}
	}
	
	private static String getContent(Field field, Object dynamicKeyInstance) throws Exception {
		
		field.setAccessible(true);
		
		if(PersistentObjectReference.class.isAssignableFrom(field.getType()))
			throw new Exception("Call dynamic id from 'PersistentObjectReference' not yet implemented");
				
		Object fieldContent = field.get(dynamicKeyInstance);
		
		if(fieldContent==null)
			throw new NullPointerException("the content of field '"+field.getName()+"' is null");
		
		return fieldContent.toString();
	}
	
	private static String getContent(Method method, Object dynamicKeyInstance) throws Exception {
		
		method.setAccessible(true);
		
		if(PersistentObjectReference.class.isAssignableFrom(method.getReturnType()))
			throw new Exception("Call dynamic id from 'PersistentObjectReference' not yet implemented");
				
		Object methodReturn = method.invoke(dynamicKeyInstance);
		
		if(methodReturn==null)
			throw new NullPointerException("the method '"+method.getName()+"()' returned null");
		
		return methodReturn.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	private static String getDynamicKey(PersistentEntity persistentEntityAnnotation, Object dynamicKeyInstance) throws Exception {
				
		String dynamicKey = null;
		
		if(persistentEntityAnnotation.dynamicKey_accessType().equals(DynamicKeyAccessType.FIELD)) 
			dynamicKey = getContent(getField(persistentEntityAnnotation.dynamicKey_name(), dynamicKeyInstance), dynamicKeyInstance);
		else if(persistentEntityAnnotation.dynamicKey_accessType().equals(DynamicKeyAccessType.METHOD)) 		
			dynamicKey = getContent(getMethod(persistentEntityAnnotation.dynamicKey_name(), dynamicKeyInstance), dynamicKeyInstance);
		
		if(dynamicKey==null)
			throw new Exception("Dynamic key cannot be 'null'");
		
		return dynamicKey.toString();
	}
	*/
	
	
	
	
	/*
	public static <KEY, VALUE>  PersistentMap<KEY, VALUE> getMapReference(Object dynamicKeyInstance, Map<KEY,PersistentObjectReference<VALUE>> initialInstanceImplementation) throws Exception{
		
		PersistentMap<KEY, VALUE> ret = null;
		
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		
		String callingClass = ste[2].getClassName();
		String methodName = ste[2].getMethodName();
		
		try {

			PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(dynamicKeyInstance, callingClass, methodName);
			
			PersistentObjectReference<Map<KEY,PersistentObjectReference<VALUE>>> wrappedReference =	
					new PersistentObjectReferenceImpl<Map<KEY,PersistentObjectReference<VALUE>>>(pori.getCalculatedKey())
				.setPersistentObjectReferenceInfo(pori);
			
			
			ret = new PersistentMapImpl<KEY,VALUE>(wrappedReference, initialInstanceImplementation);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		
		
		return ret;
	}
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//QUEUE e DEQUEUE
	
	
	
	
	
	
	
	
	
	
	/*
	public static <VALUE>  PersistentSet<VALUE> getSetReference(Object dynamicKeyInstance, Set<PersistentObjectReference<VALUE>> initialInstanceImplementation) throws Exception{
		
		PersistentSet<VALUE> ret = null;
		
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		
		String callingClass = ste[2].getClassName();
		String methodName = ste[2].getMethodName();
		
		try {

			PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(dynamicKeyInstance, callingClass, methodName);
			
			PersistentObjectReference<Set<PersistentObjectReference<VALUE>>> wrappedReference =	
					new PersistentObjectReferenceImpl<Set<PersistentObjectReference<VALUE>>>(pori.getCalculatedKey())
				.setPersistentObjectReferenceInfo(pori);
			
			
			ret = new PersistentSetImpl<VALUE>(wrappedReference, initialInstanceImplementation);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return ret;
	}
	*/
	
	/*
	public static <VALUE>  PersistentList<VALUE> getListReference(Object dynamicKeyInstance, List<PersistentObjectReference<VALUE>> initialInstanceImplementation) throws Exception{
		
		PersistentList<VALUE> ret = null;
		
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		
		String callingClass = ste[2].getClassName();
		String methodName = ste[2].getMethodName();
		
		try {

			PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(dynamicKeyInstance, callingClass, methodName);
			
			PersistentObjectReference<List<PersistentObjectReference<VALUE>>> wrappedReference =	
					new PersistentObjectReferenceImpl<List<PersistentObjectReference<VALUE>>>(pori.getCalculatedKey())
				.setPersistentObjectReferenceInfo(pori);
			
			
			ret = new PersistentListImpl<VALUE>(wrappedReference, initialInstanceImplementation);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return ret;
	}
	*/
	
	/*
	public static <VALUE>  PersistentCollection<VALUE> getCollectionReference(Object dynamicKeyInstance, Collection<PersistentObjectReference<VALUE>> initialInstanceImplementation) throws Exception{
		
		PersistentCollection<VALUE> ret = null;
		
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		
		String callingClass = ste[2].getClassName();
		String methodName = ste[2].getMethodName();
		
		try {

			PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(dynamicKeyInstance, callingClass, methodName);
			
			PersistentObjectReference<Collection<PersistentObjectReference<VALUE>>> wrappedReference =	
					new PersistentObjectReferenceImpl<Collection<PersistentObjectReference<VALUE>>>(pori.getCalculatedKey())
				.setPersistentObjectReferenceInfo(pori);
			
			
			ret = new PersistentCollectionImpl<VALUE>(wrappedReference, initialInstanceImplementation);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return ret;
	}
	*/
	
	
	
	
	
	
	
	//public static </*KEY,*/ VALUE>  PersistentObjectReference</*KEY,*/VALUE> getReference(Object dynamicKeyInstance) throws Exception{
		
		//PersistentObjectReference</*KEY,*/ VALUE> ret = null;
		
		//StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		
		//String callingClass = ste[2].getClassName();
		//String methodName = ste[2].getMethodName();
		
		//try {

			//PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(dynamicKeyInstance, callingClass, methodName);
			
			//ret = new PersistentObjectReferenceImpl<VALUE>(/*repoName, */pori.getCalculatedKey())
						//.setPersistentObjectReferenceInfo(pori);
			
		//} catch (Exception e) {
			//log.error(e.getMessage(), e);
			//throw e;
		//}
		//return ret;
	//}		
	
	//@SuppressWarnings("unchecked")
	//private static </*KEY,*/ VALUE> PersistentObjectReferenceInfo getPersistentObjectReferenceInfo(Object dynamicKeyInstance, String callingClass, String methodName) throws Exception{
		//PersistentObjectReferenceInfo pori = new PersistentObjectReferenceInfo();
		
		//pori.setInstanceForGenerateDynamicKey(dynamicKeyInstance);
		
		//Class<?> relationClass = Class.forName(callingClass);
		
		//pori.setRelationClass(relationClass);
		//pori.setRelationClassPersistentRepositoryConfigAnnotation(relationClass.getAnnotation(PersistentRepositoryConfig.class));
		
		//Method meth = relationClass.getDeclaredMethod(methodName);
		//meth.setAccessible(true);
		
		//if(!meth.getReturnType().equals(PersistentObjectReference.class))
		//if(!PersistentObjectReference.class.isAssignableFrom(meth.getReturnType()) &&
		//   !PersistentCollectionIterable.class.isAssignableFrom(meth.getReturnType()) &&
		//   !PersistentMap.class.isAssignableFrom(meth.getReturnType())
		//)
		//	throw new Exception("The method not return a valid PersistenceReference object!");
		
		//Type[] genRetTypes = ((ParameterizedType)meth.getGenericReturnType()).getActualTypeArguments();
		
		/*
		Class<KEY> classKey = null;
		if(Class.class.isAssignableFrom(genRetTypes[0].getClass()))
				classKey = (Class<KEY>) genRetTypes[0];
		*/
		
		//Class<VALUE> classValue = null;
		//Class<?> dd = genRetTypes[0].getClass();
		//if(Class.class.isAssignableFrom(genRetTypes[/*1*/0].getClass())) {
		//	if(!PersistentMap.class.isAssignableFrom(meth.getReturnType()))
		//		classValue = (Class<VALUE>) genRetTypes[/*1*/0];
		//	else
		//		classValue = (Class<VALUE>) genRetTypes[1];
		//} else {
		//	ParameterizedType parType = ((ParameterizedType)genRetTypes[/*1*/0]);
			
		//	pori.setValueTypeParametrized(true);
		//	pori.setTypeName(parType.getTypeName());
		//	Type rawType = parType.getRawType();
		//	pori.setRawType(rawType);
			
		//	classValue = (Class<VALUE>) rawType;
		//}
		
		//pori.setKeyType(classKey);
		//pori.setValueType(classValue);
		
		//String repoName = classValue.getCanonicalName();
		//String key = "";
		
		//PersistentRepositoryConfig prc = meth.getAnnotation(PersistentRepositoryConfig.class);
		//String pKey = prc.primaryKey();
		//PersistentEntity persistentEntityAnnotation = meth.getAnnotation(PersistentEntity.class);
		//pori.setObjectReferencePersistentRepositoryConfigAnnotation(prc);
		
		//if(!pKey.isEmpty()) {
			
			//pori.setPersistentEntityAnnotation(persistentEntityAnnotation);
		//	pori.setPrimaryKey(pKey);
			
			//Construction of key
			//key = getDynamicKeyByPattern(persistentEntityAnnotation.primaryKey(), dynamicKeyInstance);
		//	key = getDynamicKeyByPattern(pKey, dynamicKeyInstance);
			
			//if(!persistentEntityAnnotation.repoName().isEmpty()) 
				//repoName = persistentEntityAnnotation.repoName();
						
			/*
			else {
			
				//Static key, default empty String
				key = persistentEntityAnnotation.staticKey();
			
				//Dynamic key
				if(!persistentEntityAnnotation.dynamicKey_name().isEmpty() && dynamicKeyInstance!=null) 
					key = getDynamicKey(persistentEntityAnnotation, dynamicKeyInstance) + key;
			}
			*/
		//}
		
		//pori.setCalculatedKey(key);
		
		//return pori;
	//}
	
	
	
	
	
	
	//@Deprecated
	//public static <ITERABLE extends Iterable<PersistentObjectReference<VALUE>>, VALUE> PersistentCollectionReferenceImpl<ITERABLE, VALUE> getCollectionReference(
			/*ITERABLE emptyCollectionInstance,*//* Object dynamicKeyInstance) throws Exception{*/
		
	//	PersistentCollectionReferenceImpl<ITERABLE, VALUE> ret = null;
		
	//	StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		
	//	String callingClass = ste[2].getClassName();
	//	String methodName = ste[2].getMethodName();
		
	//	try {
			
	//		PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(dynamicKeyInstance, callingClass, methodName);
			
	//		ret = new PersistentCollectionReferenceImpl<>(/*emptyCollectionInstance,*/ pori.getCalculatedKey());
	//		ret.setPersistentObjectReferenceInfo(pori);
			
	//	} catch (Exception e) {
	//		log.error(e.getMessage(), e);
	//		throw e;
	//	}
		
		
	//	return ret;
	//}
	
	/*
	public static <KEY, VALUE> Collection<PersistentObjectReference<KEY,VALUE>> getCollectionReference(Object dynamicKeyInstance) throws Exception{
	
		return null;
	}
	*/
}

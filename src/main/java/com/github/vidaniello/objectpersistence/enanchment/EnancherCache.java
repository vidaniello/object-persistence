package com.github.vidaniello.objectpersistence.enanchment;

import java.util.HashMap;
import java.util.Map;

import javassist.util.proxy.ProxyFactory;

public class EnancherCache {
	
	private static EnancherCache enancherCache = new EnancherCache();
	public static EnancherCache get() {return enancherCache;}
	
	
	private Map<Class<?>,ClassConfiguration<?>> _cache = new HashMap<>();
	synchronized Map<Class<?>,ClassConfiguration<?>> getCache() {
		return _cache;
	}
	
	private Map<Class<?>,ProxyFactory> _cacheProxyFactory = new HashMap<>();
	synchronized ProxyFactory getProxyFactory(Class<?> clazz, ClassConfiguration<?> classCfg) {
		ProxyFactory ret = _cacheProxyFactory.get(clazz);
		if(ret==null) {
			ret = new ProxyFactory();
			ret.setSuperclass(clazz);
			DynamicPersistentClassMethodFilter<?> methFilter = new DynamicPersistentClassMethodFilter<>(classCfg, ret);
			ret.setFilter(methFilter);
			_cacheProxyFactory.put(clazz, ret);
		}
		return ret;
	}

}

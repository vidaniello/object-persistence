package com.github.vidaniello.objectpersistence.enanchment;

import java.util.HashMap;
import java.util.Map;

public class EnancherCache {
	
	private static EnancherCache enancherCache = new EnancherCache();
	public static EnancherCache get() {return enancherCache;}
	
	
	private Map<Class<?>,ClassConfiguration<?>> _cache;
	private synchronized Map<Class<?>,ClassConfiguration<?>> getCache() {
		if(_cache==null)
			_cache = new HashMap<>();
		return _cache;
	}

}

package com.github.vidaniello.objectpersistence;

import java.util.Map;

public interface PersistentMapIterable<KEY,VALUE> extends PersistenceIterable {

	/**
	 * Returns the instance on which to perform the CRUD operations (the Collection, the map, etc.)
	 * @return
	 * @throws Exception
	 */
	public Map<KEY,PersistentObjectReference<VALUE>> getMap() throws Exception;
	
	/**
	 * Persist the reference Map
	 * @param map
	 * @throws Exception
	 */
	public void setMap(Map<KEY,PersistentObjectReference<VALUE>> map) throws Exception;
		
	
	/**
	 * Persist the reference Map
	 * @param map
	 * @throws Exception
	 */
	public void setMapNewInstance(Class<Map<KEY,?>> mapConcreteClass) throws Exception;
}

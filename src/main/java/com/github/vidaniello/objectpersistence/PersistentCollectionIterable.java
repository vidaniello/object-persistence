package com.github.vidaniello.objectpersistence;

import java.util.Collection;

public interface PersistentCollectionIterable<E, T extends Collection<PersistentObjectReference<E>>> extends Iterable<E>, PersistenceIterable{
	
	/**
	 * Returns the instance on which to perform the CRUD operations (the Collection, the map, etc.)
	 * @return
	 * @throws Exception
	 */
	public T getCollection() throws Exception;
	
	/**
	 * Persist the reference collection
	 * @return
	 * @throws Exception
	 */
	public void setCollection(T collection) throws Exception;
	
	/**
	 * Create new empty instance
	 * @return
	 * @throws Exception
	 */
	public void setCollectionNewInstance(Class<Collection<?>> collectionConcreteClass) throws Exception;
	
}

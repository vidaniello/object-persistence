package com.github.vidaniello.objectpersistence;

public interface PersistenceIterable extends PersistentReference {
	
	public PersistentObjectReferenceInfo getOriginalPersistentObjectReferenceInfo();
	
	public Object getWrappedIterable() throws Exception;
	
}

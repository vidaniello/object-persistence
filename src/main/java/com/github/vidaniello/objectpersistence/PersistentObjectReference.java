package com.github.vidaniello.objectpersistence;

import java.io.Serializable;

public interface PersistentObjectReference</*KEY, */VALUE> extends PersistentReference, Serializable {

	//public String getRepoName();
	public PersistentObjectReferenceInfo getPersistentObjectReferenceInfo();
	public /*KEY*/String getKey();
	public VALUE getValue() throws Exception;
	public void setValue(VALUE value) throws Exception;
}

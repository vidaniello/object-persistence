package com.github.vidaniello.objectpersistence.enanchment;

import com.github.vidaniello.objectpersistence.PersistentReference;

public class EntityFieldWrapper {

	private EntityFieldConfiguration entityFieldConfiguration;
	private PersistentReference persistentReference;
	
	private EntityFieldWrapper() {
		
	}
	
	EntityFieldWrapper(EntityFieldConfiguration entityFieldConfiguration) {
		this.entityFieldConfiguration = entityFieldConfiguration;
	}
	
	public EntityFieldConfiguration getEntityFieldConfiguration() {
		return entityFieldConfiguration;
	}
	
	public PersistentReference getPersistentReference() {
		return persistentReference;
	}
	
	public void setPersistentReference(PersistentReference persistentReference) {
		this.persistentReference = persistentReference;
	}
	
	
}

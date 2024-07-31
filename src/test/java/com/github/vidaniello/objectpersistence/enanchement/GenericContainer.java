package com.github.vidaniello.objectpersistence.enanchement;

import java.io.Serializable;

public class GenericContainer<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private T wrappedObject;
	
	public GenericContainer() {
		
	}
	
	public T getWrappedObject() {
		return wrappedObject;
	}
	
	public void setWrappedObject(T wrappedObject) {
		this.wrappedObject = wrappedObject;
	}
}

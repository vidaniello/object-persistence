package com.github.vidaniello.objectpersistence.enanchement;

public class GenericContainer<T> {

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

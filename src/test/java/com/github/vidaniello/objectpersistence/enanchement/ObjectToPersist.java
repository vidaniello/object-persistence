package com.github.vidaniello.objectpersistence.enanchement;

import java.io.Serializable;

public class ObjectToPersist implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private byte[] data;
	private String type;
	private long lenght;
	
	public ObjectToPersist() {
		
	}
	
	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public long getLenght() {
		return lenght;
	}
	
	public void setLenght(long lenght) {
		this.lenght = lenght;
	}
	

	
}

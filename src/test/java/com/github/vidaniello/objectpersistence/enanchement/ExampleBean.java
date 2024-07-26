package com.github.vidaniello.objectpersistence.enanchement;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.vidaniello.objectpersistence.PersistentRepositoryConfig;

public class ExampleBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String field1;
	private Float field2;
	private boolean a;
	private String field4;
	
	@PersistentRepositoryConfig
	private transient ObjectToPersist objectToPersist;
	
	@PersistentRepositoryConfig
	private transient GenericContainer<ObjectToPersist> objectToPersistWrapped;
	
	@PersistentRepositoryConfig
	private transient GenericContainer<List<ObjectToPersist>> listObjectToPersistWrapped;
	
	@PersistentRepositoryConfig
	private transient List<ObjectToPersist> listObjectToPersist;
	
	@PersistentRepositoryConfig
	private transient ObjectToPersist objectToPersist2;
	
	@PersistentRepositoryConfig
	private transient Set<ObjectToPersist> setObjectToPersist;
	
	@PersistentRepositoryConfig
	private transient Map<Integer,ObjectToPersist> mapObjectToPersist;

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public Float getField2() {
		return field2;
	}

	public void setField2(Float field2) {
		this.field2 = field2;
	}

	public boolean isA() {
		return a;
	}

	public void setA(boolean a) {
		this.a = a;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	public ObjectToPersist getObjectToPersist() {
		return objectToPersist;
	}

	public void setObjectToPersist(ObjectToPersist objectToPersist) {
		this.objectToPersist = objectToPersist;
	}
	
	public GenericContainer<ObjectToPersist> getObjectToPersistWrapped() {
		return objectToPersistWrapped;
	}
	
	public Set<ObjectToPersist> getSetObjectToPersist() {
		return setObjectToPersist;
	}
	
	public GenericContainer<List<ObjectToPersist>> getListObjectToPersistWrapped() {
		return listObjectToPersistWrapped;
	}
	
	public void setListObjectToPersistWrapped(GenericContainer<List<ObjectToPersist>> listObjectToPersistWrapped) {
		this.listObjectToPersistWrapped = listObjectToPersistWrapped;
	}

	public List<ObjectToPersist> getListObjectToPersist() {
		return listObjectToPersist;
	}

	public void setListObjectToPersist(List<ObjectToPersist> listObjectToPersist) {
		this.listObjectToPersist = listObjectToPersist;
	}

	public ObjectToPersist getObjectToPersist2() {
		return objectToPersist2;
	}

	public void setObjectToPersist2(ObjectToPersist objectToPersist2) {
		this.objectToPersist2 = objectToPersist2;
	}
	
	public Map<Integer, ObjectToPersist> getMapObjectToPersist() {
		return mapObjectToPersist;
	}
	
	public void setMapObjectToPersist(Map<Integer, ObjectToPersist> mapObjectToPersist) {
		this.mapObjectToPersist = mapObjectToPersist;
	}

}

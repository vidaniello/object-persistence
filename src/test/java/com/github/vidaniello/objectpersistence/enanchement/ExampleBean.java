package com.github.vidaniello.objectpersistence.enanchement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.github.vidaniello.objectpersistence.DiskPersistManagerDefault;
import com.github.vidaniello.objectpersistence.PersistentRepositoryConfig;
import com.github.vidaniello.objectpersistence.Property;

public class ExampleBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String field1;
	private Float field2;
	private boolean a;
	private String field4;
	
	@PersistentRepositoryConfig(
			repoName = "ExampleBean.${getId()}.objectToPersist",
			//primaryKey = "collectionOfSimplePojos",
			repositoryClassImplementation = DiskPersistManagerDefault.class,
			properties = {
					@Property(key = DiskPersistManagerDefault.propertyName_repositoryPath, value = "ExampleBean/${getId()}/")
			})
	private transient ObjectToPersist objectToPersist;
	
	@PersistentRepositoryConfig(
			repoName = "ExampleBean.${getId()}.objectToPersistWrapped",
			repositoryClassImplementation = DiskPersistManagerDefault.class,
			properties = {
					@Property(key = DiskPersistManagerDefault.propertyName_repositoryPath, value = "ExampleBean/${getId()}/")
			})
	private transient GenericContainer<ObjectToPersist> objectToPersistWrapped;
	
	@PersistentRepositoryConfig(
			repoName = "ExampleBean.${getId()}.listObjectToPersistWrapped",
			repositoryClassImplementation = DiskPersistManagerDefault.class,
			properties = {
					@Property(key = DiskPersistManagerDefault.propertyName_repositoryPath, value = "ExampleBean/${getId()}/")
			})
	private transient GenericContainer<List<ObjectToPersist>> listObjectToPersistWrapped;
	
	@PersistentRepositoryConfig(
			repoName = "ExampleBean.${getId()}.listObjectToPersist",
			repositoryClassImplementation = DiskPersistManagerDefault.class,
			properties = {
					@Property(key = DiskPersistManagerDefault.propertyName_repositoryPath, value = "ExampleBean/${getId()}/listObjectToPersist/")
			})
	private transient List<ObjectToPersist> listObjectToPersist;
	
	@PersistentRepositoryConfig
	private transient ObjectToPersist objectToPersist2;
	
	@PersistentRepositoryConfig(
			repoName = "ExampleBean.${getId()}.setObjectToPersist",
			repositoryClassImplementation = DiskPersistManagerDefault.class,
			properties = {
					@Property(key = DiskPersistManagerDefault.propertyName_repositoryPath, value = "ExampleBean/${getId()}/setObjectToPersist/")
			})
	private transient Set<ObjectToPersist> setObjectToPersist;
	
	@PersistentRepositoryConfig(
			repoName = "ExampleBean.${getId()}.mapObjectToPersist",
			repositoryClassImplementation = DiskPersistManagerDefault.class,
			properties = {
					@Property(key = DiskPersistManagerDefault.propertyName_repositoryPath, value = "ExampleBean/${getId()}/mapObjectToPersist/")
			})
	private transient Map<Integer,ObjectToPersist> mapObjectToPersist;
	
	public ExampleBean() {
		
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

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
	
	public void setObjectToPersistWrapped(GenericContainer<ObjectToPersist> objectToPersistWrapped) {
		this.objectToPersistWrapped = objectToPersistWrapped;
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
		if(listObjectToPersist==null)
			listObjectToPersist = new ArrayList<>();
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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExampleBean other = (ExampleBean) obj;
		return Objects.equals(id, other.id);
	}

	
}

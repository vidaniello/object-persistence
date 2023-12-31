package com.github.vidaniello.objectpersistence;

import java.io.IOException;
import java.io.Serializable;

public interface PersistManager</*KEY, */VALUE> {
	
	public String getRepoName();
	
	//public void write(PersistentObjectReference<KEY,VALUE> object) throws IOException;
	public void write(/*KEY*/String key, VALUE value) throws IOException;
	
	//public VALUE read(PersistentObjectReference<KEY,VALUE> object) throws IOException, ClassNotFoundException;
	public VALUE read(/*KEY*/String key) throws IOException, ClassNotFoundException;
	
	//public void delete(PersistentObjectReference<KEY,VALUE> object) throws IOException;
	public void delete(/*KEY*/String key) throws IOException;

}

package com.github.vidaniello.objectpersistence;

import java.io.Serializable;

public interface PersistentObject<KEY extends Serializable> extends Serializable {

	public KEY getKey();
}

package com.github.vidaniello.objectpersistence;

import java.util.List;

public interface PersistentList<E> extends List<E>, PersistentCollectionIterable<E, List<PersistentObjectReference<E>>> {
	
}

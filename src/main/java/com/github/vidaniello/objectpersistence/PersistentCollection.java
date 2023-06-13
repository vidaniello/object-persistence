package com.github.vidaniello.objectpersistence;

import java.util.Collection;

public interface PersistentCollection<E> extends Collection<E>, PersistentCollectionIterable<E, Collection<PersistentObjectReference<E>>> {
	
}

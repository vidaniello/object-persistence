package com.github.vidaniello.objectpersistence;

@Deprecated
public class PersistentCollectionReferenceImpl<ITERABLE extends Iterable<PersistentObjectReference<VALUE>>, VALUE> 
	extends PersistentObjectReferenceImpl</*String,*/ PersistentObjectReference<ITERABLE>> {

	private static final long serialVersionUID = 1L;

	//private Class<ITERABLE> implementationCollectionClass;
	//private Iterable<PersistentObjectReference<String, VALUE>> coll;
	
	//private PersistentObjectReference</*String,*/ Iterable<PersistentObjectReference</*String,*/ VALUE>>> collRef;
	
	public PersistentCollectionReferenceImpl(/*ITERABLE emptyCollectionInstance, */String key) {
		super(key);
	}
	
	/*
	private synchronized PersistentObjectReference</*String,*//* Iterable<PersistentObjectReference</*String,*//* VALUE>>> getCollRef() throws Exception {
		if(collRef==null) {
			
			String keyOfCollection = getKey()+"."+getKey();
			
			PersistentObjectReferenceInfo pori = (PersistentObjectReferenceInfo) getPersistentObjectReferenceInfo().clone();
			
			pori.setCalculatedKey(keyOfCollection);
			
			PersistentObjectReferenceImpl<Iterable<PersistentObjectReference<VALUE>>> d = 
					new PersistentObjectReferenceImpl<>(keyOfCollection);
			
			
			
			collRef = d.setPersistentObjectReferenceInfo(pori);
		}
		return collRef;
	}
	*/
	
	@Override
	public PersistentObjectReference<ITERABLE> getValue() throws Exception {
		PersistentObjectReference<ITERABLE> ret = super.getValue();
		//if(ret==null)
			//super.setValue(this);
		return ret;
	}
	
}

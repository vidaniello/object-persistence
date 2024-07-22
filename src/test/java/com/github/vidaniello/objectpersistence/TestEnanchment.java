package com.github.vidaniello.objectpersistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.github.vidaniello.objectpersistence.enanchment.DynamicPersistentClassHandler;

public class TestEnanchment {
	
	static {
		// Log4j from 2.17.>
		//System.setProperty("log4j2.Configuration.allowedProtocols", "http");

		// URL file di configurazione Log4j2
		System.setProperty("log4j.configurationFile", "https://gist.github.com/vidaniello/c20e29cdffb407ec5d3c773fb92786b9/raw/92c8e809f51133ef56e4867a6cabb0744ee6b9b6/log4j2.xml");

		// Tips per java.util.logging
		System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");

		// private org.apache.logging.log4j.Logger log =
		// org.apache.logging.log4j.LogManager.getLogger();
		
	}
	
	private Logger log = LogManager.getLogger();
		
	@Test
	public void test1() {
		try {			
			
			String example = "getSomeField";
			String resp = DynamicPersistentClassHandler.geFieldName(example, false);
			Assert.assertTrue("someField".equals(resp));
			
			example = "setSomeField";
			resp = DynamicPersistentClassHandler.geFieldName(example, false);
			Assert.assertTrue("someField".equals(resp));
			
			example = "isSomeField";
			resp = DynamicPersistentClassHandler.geFieldName(example, false);
			Assert.assertTrue("someField".equals(resp));
			
			example = "getsomeField";
			resp = DynamicPersistentClassHandler.geFieldName(example, false);
			Assert.assertTrue("someField".equals(resp));
			
			example = "getSOmeField";
			resp = DynamicPersistentClassHandler.geFieldName(example, false);
			Assert.assertTrue("sOmeField".equals(resp));
			
			example = "getSOmeField";
			resp = DynamicPersistentClassHandler.geFieldName(example, false);
			Assert.assertTrue("sOmeField".equals(resp));
			
			example = "SOmeField";
			resp = DynamicPersistentClassHandler.geFieldName(example, false);
			Assert.assertTrue("SOmeField".equals(resp));
			
			example = "x";
			resp = DynamicPersistentClassHandler.geFieldName(example, false);
			Assert.assertTrue("x".equals(resp));
			
			example = "x";
			resp = DynamicPersistentClassHandler.geFieldName(example, true);
			Assert.assertTrue(resp==null);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new AssertionError(e);
		}
	}
	
	@Test
	public void test2() {
		try {			
			
			
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new AssertionError(e);
		}
	}
	
}

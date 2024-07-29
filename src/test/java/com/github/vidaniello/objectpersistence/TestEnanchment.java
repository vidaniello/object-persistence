package com.github.vidaniello.objectpersistence;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.github.vidaniello.objectpersistence.enanchement.ExampleBean;
import com.github.vidaniello.objectpersistence.enanchment.ClassConfiguration;
import com.github.vidaniello.objectpersistence.enanchment.DynamicPersistentClassMethodHandler;
import com.github.vidaniello.objectpersistence.enanchment.Enancher;

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
			String resp = Enancher.geFieldName(example, false);
			Assert.assertTrue("someField".equals(resp));
			
			example = "setSomeField";
			resp = Enancher.geFieldName(example, false);
			Assert.assertTrue("someField".equals(resp));
			
			example = "isSomeField";
			resp = Enancher.geFieldName(example, false);
			Assert.assertTrue("someField".equals(resp));
			
			example = "getsomeField";
			resp = Enancher.geFieldName(example, false);
			Assert.assertTrue("someField".equals(resp));
			
			example = "getSOmeField";
			resp = Enancher.geFieldName(example, false);
			Assert.assertTrue("sOmeField".equals(resp));
			
			example = "getSOmeField";
			resp = Enancher.geFieldName(example, false);
			Assert.assertTrue("sOmeField".equals(resp));
			
			example = "SOmeField";
			resp = Enancher.geFieldName(example, false);
			Assert.assertTrue("SOmeField".equals(resp));
			
			example = "x";
			resp = Enancher.geFieldName(example, false);
			Assert.assertTrue("x".equals(resp));
			
			example = "x";
			resp = Enancher.geFieldName(example, true);
			Assert.assertTrue(resp==null);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new AssertionError(e);
		}
	}
	
	@Test
	public void test2() {
		try {			
			
			ClassConfiguration<ExampleBean> cc = Enancher.getEnanchedClassConfiguration(ExampleBean.class);
			
			Assert.assertTrue(cc!=null);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new AssertionError(e);
		}
	}
	
	@Test
	public void test2_5() {
		try {			
			
			ExampleBean eb1 = new ExampleBean();
			
			Field a = eb1.getClass().getDeclaredField("objectToPersist");
			Type ta = a.getGenericType();
			Class<?> ca = ta.getClass();
			Assert.assertTrue(Class.class.isAssignableFrom(ca));
			Assert.assertTrue(!ParameterizedType.class.isAssignableFrom(ca));
			
			Field b = eb1.getClass().getDeclaredField("listObjectToPersist");
			Type tb = b.getGenericType();
			Class<?> cb = tb.getClass();
			Assert.assertTrue(ParameterizedType.class.isAssignableFrom(cb));
			Assert.assertTrue(!Class.class.isAssignableFrom(cb));
			
			Field c = eb1.getClass().getDeclaredField("objectToPersistWrapped");
			Type tc = c.getGenericType();
			Class<?> cc = tc.getClass();
			Assert.assertTrue(ParameterizedType.class.isAssignableFrom(cc));
			
			Field d = eb1.getClass().getDeclaredField("listObjectToPersistWrapped");
			Type td = d.getGenericType();
			Class<?> cd = td.getClass();
			Assert.assertTrue(ParameterizedType.class.isAssignableFrom(cd));
			
			Field e = eb1.getClass().getDeclaredField("mapObjectToPersist");
			Type te = e.getGenericType();
			Class<?> ce = te.getClass();
			Assert.assertTrue(ParameterizedType.class.isAssignableFrom(ce));
			Type[] ceTypes = ((ParameterizedType)te).getActualTypeArguments();
			Assert.assertTrue(ceTypes.length==2);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new AssertionError(e);
		}
	}
	
	@Test
	public void test3() {
		try {			
			
			ExampleBean eb1 = Enancher.getNewProxyInstance(ExampleBean.class);
			
			ExampleBean eb2 = Enancher.getNewProxyInstance(ExampleBean.class);
			
			eb1.getField1();
			
			new Thread(()->{
				eb1.getListObjectToPersistWrapped();
			}).start();
			/*
			new Thread(()->{
				eb1.getObjectToPersist();
			}).start();
			*/
			int i = 0;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new AssertionError(e);
		}
	}
	
}

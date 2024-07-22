package com.github.vidaniello.objectpersistence.enanchement;

import com.github.vidaniello.objectpersistence.enanchment.LinkedField;

public class ExampleBean {
	
	private String field1;
	private Float field2;
	private boolean a;
	private String field4;
	
	
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
	
	@LinkedField("field4")
	public String notConventionalGetter() {
		return field4;
	}
	
	public void setField4(String field4) {
		this.field4 = field4;
	}

}

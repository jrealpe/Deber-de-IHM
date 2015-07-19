package com.kokoa.huecapp.classes;

public class Category {
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private int id; 
	private String name;

	
	public Category(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}

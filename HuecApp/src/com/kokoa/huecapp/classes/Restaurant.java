package com.kokoa.huecapp.classes;

public class Restaurant {

	private int id;
	private String name;
	private String place;
	private String description;
	private float latitude;
	private float longitude;
	private String image_restaurant;

	//public Restaurant(int id, String name, String place, String schedule, String description, float latitude,
	public Restaurant(int id, String name, String place, String description, float latitude,
			float longitude, String image_restaurant) {
		
		this.id = id;
		this.name = name;
		this.place = place; 
		this.description = description;
		this.latitude = latitude; 
		this.longitude = longitude;
		this.image_restaurant = image_restaurant;

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getImage_restaurant() {
		return image_restaurant;
	}

	public void setImage_restaurant(String image_restaurant) {
		this.image_restaurant = image_restaurant;
	}
	
	@Override
	public String toString() {
		
		return "Nombre: " + getName() + "Descripcion: " + getDescription() + "Lugar: " + getPlace();
	}
	
}

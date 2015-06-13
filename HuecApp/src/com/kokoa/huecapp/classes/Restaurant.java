package com.kokoa.huecapp.classes;

public class Restaurant {

	private int id;
	private String name;
	private String place;
	private float latitude;
	private float longitude;
	private String image_restaurant;

	public Restaurant(int id, String name, String place, float latitude,
			float longitude, String image_restaurant) {
		
		this.id = id;
		this.name = name;
		this.place = place; 
		this.latitude = latitude; 
		this.longitude = longitude;
		this.image_restaurant = image_restaurant;

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
	
	

}

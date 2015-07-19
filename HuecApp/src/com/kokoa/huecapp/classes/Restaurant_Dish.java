package com.kokoa.huecapp.classes;

public class Restaurant_Dish {
	
	int id, id_res;
	String name,foto,costo;
	
	public Restaurant_Dish(int id, int id_res, String costo,
			String name, String foto) {
		super();
		this.id = id;
		this.id_res = id_res;
		this.costo = costo;
		this.name = name;
		this.foto = foto;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_res() {
		return id_res;
	}
	public void setId_res(int id_res) {
		this.id_res = id_res;
	}
	
	public String getCosto() {
		return costo;
	}
	public void setCosto(String costo) {
		this.costo = costo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}

	
}

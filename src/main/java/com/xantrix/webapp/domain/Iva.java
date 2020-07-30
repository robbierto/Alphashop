package com.xantrix.webapp.domain;

import java.io.Serializable;

public class Iva implements Serializable
{
	 
	private static final long serialVersionUID = 4157166221401288115L;
	
	private int id;
	private String descrizione;
	private int aliquota;
	
	public Iva() {}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	public int getAliquota() {
		return aliquota;
	}
	
	public void setAliquota(int aliquota) {
		this.aliquota = aliquota;
	}
	
	
	
	
	
}

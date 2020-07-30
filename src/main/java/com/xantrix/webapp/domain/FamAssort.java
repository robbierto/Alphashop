package com.xantrix.webapp.domain;

import java.io.Serializable;

public class FamAssort implements Serializable
{
	private static final long serialVersionUID = 3788120361600509595L;
	
	private int id;
	private String descrizione;
	
	public FamAssort() {}

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
	
	
		
}

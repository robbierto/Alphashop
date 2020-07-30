package com.xantrix.webapp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xantrix.webapp.validator.CodArt;

@XmlRootElement
public class Articoli implements Serializable
{
	private static final long serialVersionUID = -667971422269719485L;
	
	private int riga;
	
	//https://howtodoinjava.com/hibernate/hibernate-validator-java-bean-validation/
	
	@NotEmpty(message = "{NotEmpty.Articoli.codArt.validation}")
	@Size(min=4, max=20, message="{Size.Articoli.codArt.validation}")
	//@CodArt
	private String codArt;
	
	@Size(min=8, max=60, message = "{Size.Articoli.descrizione.validation}")
	private String descrizione;
	
	private Double prezzo;
	
	private Double prezzoKg;
	
	@NotNull(message= "{NotNull.Articoli.um.validation}") 
	private String um;
	
	private String codStat;
	
	@Max(99)
	@Digits(integer=2, fraction=0, message="{Digits.Articoli.pzCart.validation}")
	private int pzCart;
	
	@Digits(integer=4, fraction=3, message="{Digits.Articoli.pesoNetto.validation}")
	private double pesoNetto;
	
	private float qtaMag;
	
	@NotNull(message= "{NotNull.Articoli.idIva.validation}") 
	private int idIva;
	
	@NotNull(message= "{NotNull.Articoli.idStatoArt.validation}") 
	private String idStatoArt;
	
	private Date dataCreaz;
	
	@NotNull(message= "{NotNull.Articoli.idFamAss.validation}")
	private int idFamAss;
	
	private String desFamAss;
	
	@JsonIgnore
	private MultipartFile Immagine; 
	
	public Articoli()
	{
	}
	
	@XmlTransient
	public MultipartFile getImmagine() {
		return Immagine;
	}

	public void setImmagine(MultipartFile immagine) {
		Immagine = immagine;
	}

	public String getCodArt() {
		return codArt;
	}

	public void setCodArt(String codArt) {
		this.codArt = codArt;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	public String getCodStat() {
		return codStat;
	}

	public void setCodStat(String codStat) {
		this.codStat = codStat;
	}

	public int getPzCart() {
		return pzCart;
	}

	public void setPzCart(int pzCart) {
		this.pzCart = pzCart;
	}

	public double getPesoNetto() {
		return pesoNetto;
	}

	public void setPesoNetto(double pesoNetto) {
		this.pesoNetto = pesoNetto;
	}

	public int getIdIva() {
		return idIva;
	}

	public void setIdIva(int idIva) {
		this.idIva = idIva;
	}

	public String getIdStatoArt() {
		return idStatoArt;
	}

	public void setIdStatoArt(String idStatoArt) {
		this.idStatoArt = idStatoArt;
	}

	public Date getDataCreaz() {
		return dataCreaz;
	}

	public void setDataCreaz(Date dataCreaz) {
		this.dataCreaz = dataCreaz;
	}

	public int getIdFamAss() {
		return idFamAss;
	}

	public void setIdFamAss(int idFamAss) {
		this.idFamAss = idFamAss;
	}

	public int getRiga() {
		return riga;
	}

	public void setRiga(int riga) {
		this.riga = riga;
	}

	public Double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Double prezzo) {
		this.prezzo = prezzo;
	}

	public Double getPrezzoKg() {
		return prezzoKg;
	}

	public void setPrezzoKg(Double prezzoKg) {
		this.prezzoKg = prezzoKg;
	}

	public float getQtaMag() {
		return qtaMag;
	}

	public void setQtaMag(float qtaMag) {
		this.qtaMag = qtaMag;
	}

	public String getDesFamAss() {
		return desFamAss;
	}

	public void setDesFamAss(String desFamAss) {
		this.desFamAss = desFamAss;
	}
	
	

}

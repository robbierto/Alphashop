package com.xantrix.webapp.controller;

public class PagingLink 
{
	private String link;
	private String value;
	
	public PagingLink(String Link, String Value)
	{
		this.link = Link;
		this.value = Value;
	}
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}

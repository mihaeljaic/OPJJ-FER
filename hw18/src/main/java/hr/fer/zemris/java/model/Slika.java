package hr.fer.zemris.java.model;

import java.util.List;

public class Slika {
	private String staza;
	private String opis;
	private List<String> tagovi;
	
	public Slika(String staza, String opis, List<String> tagovi) {
		super();
		this.staza = staza;
		this.opis = opis;
		this.tagovi = tagovi;
	}
	
}

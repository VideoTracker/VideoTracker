package com.udem.videotracker;

public class Video {
	private String titre;
	private String description;
	private String url;
	private String auteur;
	private boolean favori;
	private int nbVues;
 
	public Video(String titre, String description, String url, String auteur, boolean favori, int nbVues){
		this.setTitre(titre);
		this.description=description;
		this.url=url;
		this.auteur=auteur;
		this.favori=favori;
		this.nbVues=nbVues;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public boolean isFavori() {
		return favori;
	}

	public void setFavori(boolean favori) {
		this.favori = favori;
	}

	public int getNbVues() {
		return nbVues;
	}

	public void setNbVues(int nbVues) {
		this.nbVues = nbVues;
	}

}

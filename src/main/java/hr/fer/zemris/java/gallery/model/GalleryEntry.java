package hr.fer.zemris.java.gallery.model;

import java.util.List;
import java.util.Set;

/**
 * Razred koji modelira jedan opisnik slike. Primjerci ovog razreda sadrži naziv
 * slike, opis slike te {@link List} oznaka uz sliku. Razred se koristi kao
 * pomoćna struktura podataka ove aplikacije.
 * 
 * @author Davor Češljaš
 */
public class GalleryEntry {

	/** Članska varijabla koja predstavlja naziv slike u memoriji */
	private String pictureName;

	/** Članska varijabla koja predstavlja opis slike */
	private String description;

	/**
	 * Članska varijabla koja predstavlja {@link Set} svih oznaka koje se vežu
	 * uz sliku
	 */
	private Set<String> tags;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Unutar ovog
	 * konstruktora svi predani parametri initerno se pohranjuju u pripadne
	 * članske varijable.
	 *
	 * @param pictureName
	 *            naziv slike u memoriji
	 * @param description
	 *            opis slike
	 * @param tags
	 *            {@link Set} svih oznaka koje se vežu uz sliku
	 */
	public GalleryEntry(String pictureName, String description, Set<String> tags) {
		super();
		this.pictureName = pictureName;
		this.description = description;
		this.tags = tags;
	}

	/**
	 * Metoda koja dohvaća naziv slike u memoriji
	 *
	 * @return naziv slike u memoriji
	 */
	public String getPictureName() {
		return pictureName;
	}

	/**
	 * Metoda koja dohvaća opis slike
	 *
	 * @return opis slike
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Metoda koja dohvaća {@link Set} svih oznaka koje se vežu uz sliku
	 *
	 * @return {@link Set} svih oznaka koje se vežu uz sliku
	 */
	public Set<String> getTags() {
		return tags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pictureName == null) ? 0 : pictureName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GalleryEntry other = (GalleryEntry) obj;
		if (pictureName == null) {
			if (other.pictureName != null)
				return false;
		} else if (!pictureName.equals(other.pictureName))
			return false;
		return true;
	}

}

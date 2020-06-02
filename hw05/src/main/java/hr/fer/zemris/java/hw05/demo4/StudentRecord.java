package hr.fer.zemris.java.hw05.demo4;

/**
 * Razred koji sadrži podatke o studentu.
 * 
 * @author Mihael Jaić
 *
 */

public class StudentRecord {
	/**
	 * jmbag.
	 */
	private String jmbag;
	/**
	 * Prezime.
	 */
	private String prezime;
	/**
	 * Ime.
	 */
	private String ime;
	/**
	 * Bodovi na međuispitu.
	 */
	private double bodoviMI;
	/**
	 * Bodovi na završnom ispitu.
	 */
	private double bodoviZI;
	/**
	 * Bodovi na labosu.
	 */
	private double bodoviLab;
	/**
	 * Ocjena.
	 */
	private int ocjena;

	/**
	 * Konstruktor u kojem se inicijaliziraju vrijednosti svih atributa
	 * studenta.
	 * 
	 * @param jmbag
	 *            jmbag.
	 * @param prezime
	 *            Prezime.
	 * @param ime
	 *            Ime.
	 * @param bodoviMI
	 *            Bodovi na međuispitu.
	 * @param bodoviZI
	 *            Bodovi na završnom ispitu.
	 * @param bodoviLab
	 *            Bodovi na labosu.
	 * @param ocjena
	 *            Ocjena
	 * @throws IllegalArgumentException
	 *             Ako je neki od String elemenata null.
	 */

	public StudentRecord(String jmbag, String prezime, String ime, double bodoviMI, double bodoviZI, double bodoviLab,
			int ocjena) throws IllegalArgumentException {
		super();
		if (jmbag == null || prezime == null || ime == null) {
			throw new IllegalArgumentException("Nisu dozvoljene null vrijednosti.");
		}

		this.jmbag = jmbag;
		this.prezime = prezime;
		this.ime = ime;
		this.bodoviMI = bodoviMI;
		this.bodoviZI = bodoviZI;
		this.bodoviLab = bodoviLab;
		this.ocjena = ocjena;
	}

	/**
	 * Vraća jmbag.
	 * 
	 * @return jmbag.
	 */

	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Vraća prezime.
	 * 
	 * @return Prezime.
	 */

	public String getPrezime() {
		return prezime;
	}

	/**
	 * Vraća ime.
	 * 
	 * @return Ime.
	 */

	public String getIme() {
		return ime;
	}

	/**
	 * Vraća broj bodova na međuispitu.
	 * 
	 * @return Broj bodova na međuispitu.
	 */

	public double getBodoviMI() {
		return bodoviMI;
	}

	/**
	 * Vraća broj bodova na završnom ispitu.
	 * 
	 * @return Broj bodova na završnom ispitu.
	 */

	public double getBodoviZI() {
		return bodoviZI;
	}

	/**
	 * Vraća broj bodova na labosu.
	 * 
	 * @return Broj bodova na labosu.
	 */

	public double getBodoviLab() {
		return bodoviLab;
	}

	/**
	 * Vraća ocjenu.
	 * 
	 * @return Ocjena.
	 */

	public int getOcjena() {
		return ocjena;
	}

	/**
	 * Vraća ukupan broj bodova stečenih na međuispitu, završnom ispitu i
	 * labosu.
	 * 
	 * @return Ukupan broj bodova stečenih na međuispitu, završnom ispitu i
	 *         labosu.
	 */

	public double getUkupnobodova() {
		return bodoviMI + bodoviZI + bodoviLab;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jmbag == null) ? 0 : jmbag.hashCode());
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
		StudentRecord other = (StudentRecord) obj;
		if (jmbag == null) {
			if (other.jmbag != null)
				return false;
		} else if (!jmbag.equals(other.jmbag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s %s %s %.2f %.2f %.2f %d", jmbag, prezime, ime, bodoviMI, bodoviZI, bodoviLab, ocjena);
	}

}

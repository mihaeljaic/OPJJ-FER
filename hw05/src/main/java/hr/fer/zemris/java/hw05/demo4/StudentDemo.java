package hr.fer.zemris.java.hw05.demo4;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Program učitava tekstualnu datoteku sa zapisima o studentima(studenti.txt) te
 * ispisuje: 
 * -Broj studenata koji u sumi MI+ZI+LAB imaju više od 25 bodova.
 * -Broj studenata koji su dobili ocjenu 5.
 * -Sve podatke studenata koji su dobili ocjenu 5. 
 * -Sve podatke studenata koji su dobili ocjenu 5 sortirani silazno po ukupnom broju bodova.
 * -Listu jmbag-ova studenata koji nisu položili sortiranu uzlazno. 
 * -Za svaku određenu ocjenu ispisuje sve studente koji su ju dobili.
 * -Za svaku određenu ocjenu ispisuje broj studenata koji su ju dobili.
 * -Podatke o studentima koji nisu položili i koji jesu položili odvojeno.
 * 
 * @author Mihael Jaić
 *
 */

public class StudentDemo {

	/**
	 * Metoda od koje kreće izvođenje programa.
	 * 
	 * @param args
	 *            Argumenti komandne linije.
	 */

	public static void main(String[] args) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get("./src/main/resources/studenti.txt"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		List<StudentRecord> records = null;
		try {
			records = convert(lines);
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
			System.exit(1);
		}

		long broj = vratiBodovaViseOd25(records);
		System.out.printf("Broj studenata koji imaju ukupno više od 25 bodova: %d%n", broj);

		long broj5 = vratiBrojOdlikasa(records);
		System.out.printf("Broj odlikaša: %d%n", broj5);

		List<StudentRecord> odlikasi = vratiListuOdlikasa(records);
		System.out.println("Lista odlikaša:");
		odlikasi.forEach(System.out::println);

		List<StudentRecord> odlikasiSortirano = vratiSortiranuListuOdlikasa(records);
		System.out.printf("%nOdlikaši sortirano po ukupnom broju bodova:%n");
		odlikasiSortirano.forEach(System.out::println);

		List<String> nepolozeniJMBAGovi = vratiPopisNepolozenih(records);
		System.out.printf("%nLista nepoloženih(%d):%n", nepolozeniJMBAGovi.size());
		nepolozeniJMBAGovi.forEach(System.out::println);

		Map<Integer, List<StudentRecord>> mapaPoOcjenama = razvrstajStudentePoOcjenama(records);
		System.out.printf("%nStudenti po ocjenama:%n");
		mapaPoOcjenama.forEach((i, s) -> {
			System.out.printf("Ocjena %d(%d studenata):%n", i, s.size());
			s.forEach(System.out::println);
		});

		Map<Integer, Integer> mapaPoOcjenama2 = vratiBrojStudenataPoOcjenama(records);
		System.out.printf("%nBroj studenata po ocjenama:%n");
		mapaPoOcjenama2.forEach((i1, i2) -> System.out.printf("Ocjena %d: %d studenata%n", i1, i2));

		Map<Boolean, List<StudentRecord>> prolazNeprolaz = razvrstajProlazPad(records);
		System.out.println();
		prolazNeprolaz.forEach((k, v) -> {
			System.out.printf("%s(%d):%n", k ? "Prošli" : "Nisu prošli", v.size());
			v.forEach(System.out::println);
		});

	}

	/**
	 * Pretvara redak po redak predanog teksta u StudentRecord zapis. Zapisi se
	 * dodaju u listu koju metoda vraća.
	 * 
	 * @param lines
	 *            Ulazni tekst.
	 * @return Lista zapisa StudentRecord.
	 * @throws IllegalArgumentException
	 *             Ako tekst nije ispravan.
	 */

	private static List<StudentRecord> convert(List<String> lines) throws IllegalArgumentException {
		if (lines.isEmpty()) {
			throw new IllegalArgumentException("Prazan tekst.");
		}
		
		List<StudentRecord> records = new ArrayList<>();
		// Koristi se za provjeru duplikata jmbag-a.
		Set<String> jmbags = new HashSet<>();

		for (int i = 0; i < lines.size(); i++) {
			String[] attr = lines.get(i).split("\t");

			if (attr.length != 7) {
				throw new IllegalArgumentException("Tekst nema 7 atributa na liniji " + (i + 1));
			}
			
			if (jmbags.contains(attr[0])) {
				throw new IllegalArgumentException(
						String.format("jmbag %s već postoji. Greška na liniji %d.%n", attr[0], i + 1));
			}

			double mi, zi, lab;
			int ocjena;
			try {
				mi = Double.parseDouble(attr[3]);
				zi = Double.parseDouble(attr[4]);
				lab = Double.parseDouble(attr[5]);
				ocjena = Integer.parseInt(attr[6]);

			} catch (NumberFormatException ex) {
				throw new IllegalArgumentException("Nije uspjela pretvorba u brojeve na liniji " + (i + 1));
			}

			jmbags.add(attr[0]);
			records.add(new StudentRecord(attr[0], attr[1], attr[2], mi, zi, lab, ocjena));
		}

		return records;
	}

	/**
	 * Vraća broj studenata koji imaju više od 25 bodova ukupno.
	 * 
	 * @param records
	 *            Lista zapisa o studentima.
	 * @return Broj studenata koji imaju više od 25 bodova ukupno.
	 */

	public static long vratiBodovaViseOd25(List<StudentRecord> records) {
		return records.stream().filter(r -> r.getUkupnobodova() > 25.0).count();
	}

	/**
	 * Vraća broj studenata koji imaju ocjenu 5.
	 * 
	 * @param records
	 *            Lista zapisa o studentima.
	 * @return Broj studenata koji imaju ocjenu 5.
	 */

	public static long vratiBrojOdlikasa(List<StudentRecord> records) {
		return records.stream().filter(r -> r.getOcjena() == 5).count();
	}

	/**
	 * Vraća listu odlikaša.
	 * 
	 * @param records
	 *            Lista zapisa o studentima.
	 * @return Lista odlikaša.
	 */

	public static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records) {
		return records.stream().filter(r -> r.getOcjena() == 5).collect(Collectors.toList());
	}

	/**
	 * Vraća listu odlikaša sortiranu silazno po ukupnom broju bodova.
	 * 
	 * @param records
	 *            Lista zapisa o studentima.
	 * @return Lista odlikaša sortirana silazno po ukupnom broju bodova.
	 */

	public static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records) {
		return records.stream()
				.filter(r -> r.getOcjena() == 5)
				.sorted((r1, r2) -> Double.compare(r2.getUkupnobodova(), r1.getUkupnobodova()))
				.collect(Collectors.toList());
	}

	/**
	 * Vraća listu jmbag-ova studenata koji nisu položili kolegij sortiranu
	 * uzlazno.
	 * 
	 * @param records
	 *            Lista zapisa o studentima.
	 * @return Lista jmbag-ova studenata koji nisu položili kolegij sortirana
	 *         uzlazno.
	 */

	public static List<String> vratiPopisNepolozenih(List<StudentRecord> records) {
		return records.stream()
				.filter(r -> r.getOcjena() == 1)
				.map(r -> r.getJmbag())
				.sorted()
				.collect(Collectors.toList());
	}

	/**
	 * Vraća studente razvrstane po ocjenama.
	 * 
	 * @param records
	 *            Lista zapisa o studentima.
	 * @return Studetni razvrstani po ocjenama.
	 */

	public static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> records) {
		return records.stream().collect(Collectors.groupingBy(StudentRecord::getOcjena));
	}

	/**
	 * Vraća broj studenata za svaku određenu ocjenu.
	 * 
	 * @param records
	 *            Lista zapisa o studentima.
	 * @return Broj studenata za svaku određenu ocjenu.
	 */

	public static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> records) {
		return records.stream().collect(Collectors.toMap(StudentRecord::getOcjena, r -> 1, (s, n) -> s + 1));
	}

	/**
	 * Vraća zapise o studentima podijeljene na one koji su prošli i one koji
	 * nisu.
	 * 
	 * @param records
	 *            Lista zapisa o studentima.
	 * @return Zapisi o studentima podijeljeni na one koji su prošli i one koji
	 *         nisu.
	 */

	public static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> records) {
		return records.stream().collect(Collectors.partitioningBy(r -> r.getOcjena() > 1));
	}
}

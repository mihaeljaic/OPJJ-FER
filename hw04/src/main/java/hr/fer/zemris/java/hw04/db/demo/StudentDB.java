package hr.fer.zemris.java.hw04.db.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.List;

import hr.fer.zemris.java.hw04.db.*;

/**
 * Program that demonstrates functionality of database of student records. It
 * accepts one argument which is path to text file containing student records.
 * If ran without arguments program loads file "database.txt" from this project.
 * User can use only 2 commands: query and exit. Command query selects records
 * that satisfy expression given after query command by user and prints given
 * records in form of table. Expression has to be in form Attribute name
 * followed by operator followed by string literal. There can be multiple
 * expressions separated by only AND operator. Allowed attribute names are:
 * firstName, lastName, jmbag. Allowed operators are: "<", ">", "<=", ">=", "=",
 * "!=" and "LIKE". Command exit has obvious meaning.
 * 
 * @author Mihael Jaić
 *
 */

public class StudentDB {

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		if (args.length > 1) {
			System.err.println("Program accepts one or zero arguments.");
			System.exit(1);
		}

		StudentDatabase database = null;

		try {
			String fileName = args.length == 1 ? args[0] : "database.txt";
			database = new StudentDatabase(Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8));

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);

		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		QueryParser parser = new QueryParser();

		Scanner sc = new Scanner(System.in);
		
		System.out.println("Database is now ready. You can start typing queries.");

		while (true) {
			try {
				String query = sc.nextLine();
				if (query.equals("exit")) {
					System.out.println("Goodbye!");
					break;
				}

				if (!query.startsWith("query ")) {
					System.out.println("Invalid command. Only \"query\" and \"exit\" commands are allowed");
					continue;
				}

				parser.setQueryString(query.replace("query ", ""));
				parser.parse();

				System.out.print(tableToString(parser, database));

			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}

		sc.close();
	}

	/**
	 * Creates string representation of query table.
	 * 
	 * @param parser
	 *            Reference to parser.
	 * @param database
	 *            Reference to database.
	 * @return String representation of query table.
	 */

	private static String tableToString(QueryParser parser, StudentDatabase database) {
		StringBuilder sb = new StringBuilder();

		if (parser.isDirectQuery()) {
			sb.append(String.format("Using index for record retrieval.%n"));

			StudentRecord record = database.forJMBAG(parser.getQueriedJMBAG());
			if (record == null) {
				sb.append(String.format("Records selected: 0%n"));
				return sb.toString();
			}

			int[] lengths = new int[] { record.getJmbag().length(), record.getLastName().length(),
					record.getFirstName().length(), Integer.toString(record.getFinalGrade()).length() };

			sb.append(tableLine(lengths));

			sb.append(String.format("| %s | %s | %s | %d |%n", record.getJmbag(), record.getLastName(),
					record.getFirstName(), record.getFinalGrade()));

			sb.append(tableLine(lengths));
			sb.append(String.format("Records selected: 1%n"));

			return sb.toString();
		}

		QueryFilter filter = new QueryFilter(parser.getQuery());
		List<StudentRecord> records = database.filter(filter);

		if (records.size() == 0) {
			return String.format("Records selected: 0%n");
		}

		int[] longest = calculateLongest(records);

		sb.append(tableLine(longest));

		for (StudentRecord record : records) {
			sb.append(recordForPrint(record, longest));
		}

		sb.append(tableLine(longest));
		sb.append(String.format("Records selected: %d%n", records.size()));

		return sb.toString();
	}

	/**
	 * Returns array of longest attributes.
	 * 
	 * @param records
	 *            List of records.
	 * @return Array of longest attributes.
	 */

	private static int[] calculateLongest(List<StudentRecord> records) {
		int[] res = new int[4];

		for (StudentRecord r : records) {
			res[0] = Math.max(res[0], r.getJmbag().length());
			res[1] = Math.max(res[1], r.getLastName().length());
			res[2] = Math.max(res[2], r.getFirstName().length());
			res[3] = Math.max(res[3], Integer.toString(r.getFinalGrade()).length());
		}

		return res;
	}

	/**
	 * Used for drawing first and last line in table.
	 * 
	 * @param longestAttr
	 *            Array of longest attribute names.
	 * @return String that draws first and last line in table.
	 */

	private static String tableLine(int... longestAttr) {
		StringBuilder sb = new StringBuilder("+");

		for (int longest : longestAttr) {
			for (int i = 0; i < longest + 2; i++) {
				sb.append("=");
			}
			sb.append("+");
		}

		sb.append(String.format("%n"));

		return sb.toString();
	}

	/**
	 * Generates string representation of student record for table print.
	 * 
	 * @param record
	 *            Student record.
	 * @param longest
	 *            Array of longest attributes.
	 * @return String representation of student record for table print.
	 */

	private static String recordForPrint(StudentRecord record, int[] longest) {
		StringBuilder sb = new StringBuilder("| ");

		sb.append(record.getJmbag());
		for (int i = record.getJmbag().length(); i < longest[0] + 1; i++) {
			sb.append(" ");
		}

		sb.append("| " + record.getLastName());
		for (int i = record.getLastName().length(); i < longest[1] + 1; i++) {
			sb.append(" ");
		}

		sb.append("| " + record.getFirstName());
		for (int i = record.getFirstName().length(); i < longest[2] + 1; i++) {
			sb.append(" ");
		}

		String temp = Integer.toString(record.getFinalGrade());
		sb.append("| " + temp);
		for (int i = temp.length(); i < longest[3] + 1; i++) {
			sb.append(" ");
		}

		sb.append(String.format("|%n"));
		return sb.toString();
	}

}

package hr.fer.zemris.java.hw16.search;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Program gets argument that is path to directory in which is text data.
 * Program searches given directory and all subdirectories for text files and
 * collects all data from them in order to perform search queries.<br>
 * Use "./src/main/resources/clanci" as path.<br>
 * Program supports 4 commands:<br>
 * "query": Consists of words that are searched. Words are separated with space
 * and aren't case sensitive. Stop words aren't considered while searching.
 * Searches documents with most similarity to given query and writes out ten
 * best results, or less if there are less than ten documents that have
 * similarity greater than zero.<br>
 * "type": Accepts integer that is index of last query's result document that
 * will be printed on standard output. Index has to be in valid interval.<br>
 * "results": Prints out results of last query.<br>
 * "exit": Exits program.
 * 
 * @author Mihael Jaić
 *
 */

public class Console {
	/** Vocabulary that holds words and number of documents they are in. */
	private static Map<String, Integer> vocabulary = new HashMap<>();
	/** Stores term frequency for each word-document pair. */
	private static Map<WordDocumentPair, Integer> tf = new HashMap<>();
	/** Stores idf for all words. */
	private static Map<String, Double> idf = new HashMap<>();
	/** Stores vector norm values for each document. */
	private static Map<String, Double> vectorNorms = new HashMap<>();
	/** Stores all document paths. */
	private static List<String> documentPaths = new ArrayList<>();
	/** Document count. */
	private static int documentCount = 0;

	/** List of search results. */
	private static List<Result> results = new ArrayList<>();

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Path wasn't provided.");
			return;
		}

		try {
			getVocabulary(Paths.get(resolvePath(args)).toAbsolutePath());
		} catch (IOException e) {
			System.out.println("Invalid path. Couldn't initialize vocabulary.");
		}

		calculateIdf();
		calculateVectorNorms();

		try (Scanner sc = new Scanner(System.in)) {
			while (true) {
				System.out.print("Enter command> ");
				String input = sc.nextLine();

				String command = input.trim().toLowerCase();
				if (command.equals("exit")) {
					break;
				}

				if (command.startsWith("query")) {
					query(input);
				} else if (command.startsWith("type")) {
					type(input);
				} else if (command.equals("results")) {
					showResults(input);
				} else {
					System.out.printf("Unknown command.%n%n");
				}
			}
		}
	}

	/**
	 * Connects all arguments into single path. This is used in case path
	 * contains spaces.
	 * 
	 * @param args
	 *            Command line arguments.
	 * @return Connected path.
	 */

	private static String resolvePath(String[] args) {
		StringBuilder sb = new StringBuilder();
		for (String arg : args) {
			sb.append(arg);
		}

		return sb.toString();
	}

	/**
	 * Extracts vocabulary from all readable files in given folder and it's
	 * subfolders.
	 * 
	 * @param path
	 *            Root directory for reading file.
	 * @throws IOException
	 *             I/O error.
	 */

	private static void getVocabulary(Path path) throws IOException {
		if (!Files.isDirectory(path)) {
			System.out.println("File has to be directory.");
			System.exit(0);
		}

		Set<String> stopWords = new HashSet<>();
		for (String stopWord : Files.readAllLines(Paths.get("./src/main/resources/hrvatski_stoprijeci.txt"),
				StandardCharsets.UTF_8)) {
			stopWords.add(stopWord);
		}

		Files.walkFileTree(path, new FileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (Files.isReadable(file)) {
					List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
					String docName = file.toAbsolutePath().toString();
					documentPaths.add(docName);

					for (String line : lines) {
						for (String word : line.toLowerCase().split("\\P{L}+")) {
							if (!stopWords.contains(word)) {
								WordDocumentPair temp = new WordDocumentPair(word, docName);
								// If word already exists in document increments
								// its frequency.
								if (tf.containsKey(temp)) {
									tf.put(temp, tf.get(temp) + 1);

									// New word in this document.
								} else {
									tf.put(temp, 1);
									// If vocabulary contains this word
									// increments frequency of word in
									// documents. Otherwise inserts word with
									// frequency value 1.
									if (vocabulary.containsKey(word)) {
										vocabulary.put(word, vocabulary.get(word) + 1);
									} else {
										vocabulary.put(word, 1);
									}
								}
							}
						}
					}

					documentCount++;
				}

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});

		vocabulary.remove("");
		System.out.printf("Document count: %d%nVocabulary size is %d%n%n", documentCount, vocabulary.size());
	}

	/**
	 * Calculates idf values for all words.
	 */

	private static void calculateIdf() {
		for (Map.Entry<String, Integer> word : vocabulary.entrySet()) {
			idf.put(word.getKey(), Math.log((double) documentCount / word.getValue()));
		}
	}

	/**
	 * Calculates vector norms for all documents.
	 */

	private static void calculateVectorNorms() {
		for (String docPath : documentPaths) {
			double norm = 0.0;
			for (String word : vocabulary.keySet()) {
				WordDocumentPair temp = new WordDocumentPair(word, docPath);
				if (tf.containsKey(temp)) {
					norm += Math.pow(tf.get(temp) * idf.get(word), 2);
				}
			}

			vectorNorms.put(docPath, Math.sqrt(norm));
		}
	}

	/**
	 * Gets search results.
	 * 
	 * @param words
	 *            Query words.
	 * @return List of best results.
	 */

	private static List<Result> getSearchResults(Map<String, Integer> words) {
		List<Result> similarity = new ArrayList<>();

		for (String docPath : documentPaths) {
			double sim = calculateSimilarity(words, docPath);
			double epsilon = 1e-4;
			if (Math.abs(sim) >= epsilon) {
				similarity.add(new Result(docPath, sim));
			}
		}

		Collections.sort(similarity, (a, b) -> a.sim - b.sim < 0 ? 1 : -1);
		return similarity;
	}

	/**
	 * Calculates similarity between query vector and document vector.
	 * 
	 * @param words
	 *            Words used in query.
	 * @param docPath
	 *            Document path.
	 * @return Similarity between query and document.
	 */

	private static double calculateSimilarity(Map<String, Integer> words, String docPath) {
		double queryVectorNorm = 0.0;
		double scalarProduct = 0.0;
		for (Map.Entry<String, Integer> word : words.entrySet()) {
			double idfValue = idf.get(word.getKey());
			double queryTfidf = word.getValue() * idfValue;
			Integer docTf = tf.get(new WordDocumentPair(word.getKey(), docPath));
			double docTfidf = docTf == null ? 0.0 : docTf * idfValue;

			queryVectorNorm += (queryTfidf * queryTfidf);
			scalarProduct += (queryTfidf * docTfidf);
		}

		queryVectorNorm = Math.sqrt(queryVectorNorm);

		return scalarProduct / (queryVectorNorm * vectorNorms.get(docPath));
	}

	/**
	 * Class that models search results.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private static class Result {
		/** Path to document. */
		private String docPath;
		/** Similarity between query and this document. */
		private Double sim;

		/**
		 * Constructor that sets all attributes.
		 * 
		 * @param docPath
		 *            Path to document.
		 * @param sim
		 *            Similarity between query and this document.
		 */

		public Result(String docPath, Double sim) {
			super();
			this.docPath = docPath;
			this.sim = sim;
		}

	}

	/**
	 * Class that models word document pair.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private static class WordDocumentPair {
		/** Word. */
		private String word;
		/** Document path. */
		private String documentName;

		/**
		 * Constructor that sets all attributes.
		 * 
		 * @param word
		 * @param documentName
		 */

		public WordDocumentPair(String word, String documentName) {
			super();
			this.word = word;
			this.documentName = documentName;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((documentName == null) ? 0 : documentName.hashCode());
			result = prime * result + ((word == null) ? 0 : word.hashCode());
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
			WordDocumentPair other = (WordDocumentPair) obj;
			if (documentName == null) {
				if (other.documentName != null)
					return false;
			} else if (!documentName.equals(other.documentName))
				return false;
			if (word == null) {
				if (other.word != null)
					return false;
			} else if (!word.equals(other.word))
				return false;
			return true;
		}

	}

	/**
	 * Query command given by user. Calculates similarity with all documents and
	 * prints out 10 best results or until similarity becomes zero. Duplicate
	 * words are treated as one word.
	 * 
	 * @param input
	 *            Query from user.
	 */

	private static void query(String input) {
		if (!input.trim().toLowerCase().split("\\s+")[0].equals("query")) {
			System.out.println("Unknown command");
			return;
		}

		// Query can contain more equal words so map is used to count frequency
		// for each word.
		Map<String, Integer> words = parseQuery(input);
		System.out.printf("Query is: %s%n", queryToString(words));
		System.out.println("10 best results:");

		results.clear();

		results = getSearchResults(words);
		if (results.isEmpty()) {
			System.out.println("Found no matches.");
			return;
		}

		for (int i = 0; i < results.size() && i < 10; i++) {
			System.out.printf("[%d](%.4f) %s%n", i, results.get(i).sim, results.get(i).docPath);
		}
	}

	/**
	 * Gets all words from query.
	 * 
	 * @param words
	 *            Words mapped with their frequency in query.
	 * @return Words.
	 */

	private static String queryToString(Map<String, Integer> words) {
		StringBuilder sb = new StringBuilder("[");
		for (String word : words.keySet()) {
			sb.append(String.format("%s, ", word));
		}

		if (sb.length() > 1) {
			sb.setLength(sb.length() - 2);
		}
		sb.append("]");

		return sb.toString();
	}

	/**
	 * Gets query words.
	 * 
	 * @param input
	 *            User's query.
	 * @return Query words.
	 */

	private static Map<String, Integer> parseQuery(String input) {
		Map<String, Integer> words = new LinkedHashMap<>();

		String[] inputWords = input.toLowerCase().split("\\s+");
		for (int i = 1; i < inputWords.length; i++) {
			if (vocabulary.containsKey(inputWords[i])) {
				if (words.containsKey(inputWords[i])) {
					words.put(inputWords[i], words.get(inputWords[i]) + 1);
				} else {
					words.put(inputWords[i], 1);
				}
			}
		}

		return words;
	}

	/**
	 * Gets index of search result document from previous query and prints out
	 * all of documents content on standard output. Valid indexes are in
	 * interval[0, result's size - 1].
	 * 
	 * @param input
	 *            Users command which contains index of document in search
	 *            result that will be printed out.
	 */

	private static void type(String input) {
		if (results.isEmpty()) {
			System.out.println("No search results are available.");
			return;
		}

		String[] temp = input.trim().split("\\s+");
		if (temp.length != 2 || !temp[0].toLowerCase().equals("type")) {
			System.out.println("Type command expects one index argument.");
			return;
		}

		int index = 0;
		try {
			index = Integer.valueOf(temp[1]);
		} catch (NumberFormatException e) {
			System.out.println("Type command expects one index argument.");
			return;
		}

		if (index < 0 || index >= results.size()) {
			System.out.printf("Index is out of bounds. Legal indexes are from 0 to %d.%n", results.size() - 1);
			return;
		}

		Result result = results.get(index);

		StringBuilder separator = new StringBuilder(result.docPath.length());
		final int initialSpace = 10;
		for (int i = 0, len = result.docPath.length() + initialSpace; i < len; i++) {
			separator.append("-");
		}

		System.out.println(separator.toString());
		System.out.printf("Document: %s%n", result.docPath);
		System.out.println(separator.toString());

		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(result.docPath), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Couldn't open result document.");
			return;
		}

		for (String line : lines) {
			System.out.println(line);
		}
	}

	/**
	 * Shows results of previous query.
	 * 
	 * @param input
	 *            Input.
	 */

	private static void showResults(String input) {
		if (results.isEmpty()) {
			System.out.println("No search results are available.");
			return;
		}

		for (int i = 0; i < results.size() && i < 10; i++) {
			System.out.printf("[%d](%.4f) %s%n", i, results.get(i).sim, results.get(i).docPath);
		}
	}

}

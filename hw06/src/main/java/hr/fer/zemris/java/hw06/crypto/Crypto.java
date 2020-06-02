package hr.fer.zemris.java.hw06.crypto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Program offers 3 methods that are run with command line arguments. First
 * method calculates digest from current file using SHA-256 algorithm. Requires
 * 2 command line arguments. First argument has to be key word "checksha" and
 * second is path to file. Second method encrypts given file using AES
 * crypto-algorithm and the 128-bit encryption key into output file. Requires 3
 * arguments. First argument has to be key word "encrypt". Second argument is
 * source path and third argument is destination path. Third metho decrypts
 * given file into destination file using same algorithm. Requires 3 arguments.
 * First argument has to be key word "decrypt". Second argument is source path
 * and third argument is destination path.
 * 
 * @author Mihael Jaić
 *
 */

public class Crypto {
	/**
	 * Buffer size;
	 */
	private static final int BUFFER_SIZE = 4096;
	/**
	 * Method name used for recognizing from command line arguments.
	 */
	private static final String CHECKSA_METHOD = "checksha";
	/**
	 * Method name used for recognizing from command line arguments.
	 */
	private static final String ENCRYPT_METHOD = "encrypt";
	/**
	 * Method name used for recognizing from command line arguments.
	 */
	private static final String DECRYPT_METHOD = "decrypt";

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		if (args.length < 2 || args.length > 3) {
			System.out.println("Invalid number of arguments");
			System.exit(1);
		}

		try (Scanner sc = new Scanner(System.in)) {
			if (args[0].equals(CHECKSA_METHOD)) {
				if (args.length != 2) {
					System.out.printf("%s method receives only 1 argument.%n", CHECKSA_METHOD);
					System.exit(1);
				}

				System.out.printf("Please provide expected sha-256 digest for %s:%n> ", args[1]);

				String key = sc.nextLine();
				String digestValue = null;
				try {
					digestValue = calculateSHA(Paths.get(args[1]));

				} catch (IOException ex) {
					System.out.printf("Couldn't open file %s%n", args[1]);
					System.exit(1);
				} catch (NoSuchAlgorithmException ex) {
					System.out.println("Couldn't get algorithm");
					System.exit(1);
				} catch (InvalidPathException ex) {
					System.out.println("Invalid path");
					System.exit(1);
				}

				System.out.println(printComparison(key, digestValue, args[1]));

			} else if (args[0].equals(DECRYPT_METHOD) || args[0].equals(ENCRYPT_METHOD)) {
				if (args.length != 3) {
					System.out.printf("%s method receives 2 arguments.%n", args[0]);
					System.exit(1);
				}

				System.out.printf("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):%n> ");
				String password = sc.nextLine();
				System.out.printf("Please provide initialization vector as hex-encoded text (32 hex-digits):%n> ");
				String vector = sc.nextLine();

				try {
					crypt(Paths.get(args[1]), getCipher(password, vector, args[0]), Paths.get(args[2]));
				} catch (RuntimeException ex) {
					System.out.printf("Couldn't perform %s%n%s%n", args[0], ex.getMessage());
					System.exit(1);
				}

				System.out.printf("%sion completed. Generated file %s based on file %s.%n", args[0], args[2], args[1]);

			} else {
				System.out.printf("Unknown method name %s", args[0]);
			}
		}

	}

	/**
	 * Creates new cipher object with given parameters.
	 * 
	 * @param password
	 *            Password.
	 * @param vector
	 *            Vector.
	 * @param cryptType
	 *            Crypt type.
	 * @return New cipher object.
	 */

	private static Cipher getCipher(String password, String vector, String cryptType) {
		Cipher cipher = null;

		try {
			String keyText = password;
			String ivText = vector;
			SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));

			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(cryptType.equals(ENCRYPT_METHOD) ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec,
					paramSpec);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException ex) {
			throw new RuntimeException("Couldn't initialize cipher");
		}

		return cipher;
	}

	/**
	 * Performs encryption/decryption from given source path to destination
	 * path. Reads from source file and by using cipher object converts bytes
	 * and writes converted bytes into destination file. Source path has to
	 * exist and has to be readable. If destination file doesn't exist new file
	 * is created.
	 * 
	 * @param path
	 *            Source path.
	 * @param cipher
	 *            Cipher object.
	 * @param destination
	 *            Destination path.
	 */

	private static void crypt(Path path, Cipher cipher, Path destination) {
		if (!Files.exists(path)) {
			throw new RuntimeException(String.format("file %s doesn't exist", path.toString()));
		}

		File file = new File(destination.toAbsolutePath().toString());

		if (file.getParentFile() != null) {
			file.getParentFile().mkdirs();
		}

		try {
			file.createNewFile();
		} catch (IOException e1) {
			throw new RuntimeException("Couldn't create destination file!");
		}

		try (InputStream is = Files.newInputStream(path, StandardOpenOption.READ);
				OutputStream os = Files.newOutputStream(destination, StandardOpenOption.WRITE)) {
			byte[] buffer = new byte[BUFFER_SIZE];

			while (true) {
				int r = is.read(buffer);
				if (r < 1)
					break;

				byte[] temp;
				temp = cipher.update(buffer, 0, r);

				os.write(temp);
			}
			try {
				os.write(cipher.doFinal());
			} catch (IllegalBlockSizeException e) {
				throw new RuntimeException(e.getMessage());
			} catch (BadPaddingException e) {
				throw new RuntimeException(e.getMessage());
			}

		} catch (IOException ex) {
			throw new RuntimeException(String.format("Couldn't perform transforming from %s to %s", path.toString(),
					destination.toString()));
		}
	}

	/**
	 * Builds string for printing that informs user if his digest was correct.
	 * In case it wasn't correct digest is printed.
	 * 
	 * @param key
	 *            Key.
	 * @param digestValue
	 *            Digest value.
	 * @param fileName
	 *            File name.
	 * @return String for printing that informs user is his input was correct.
	 */

	private static String printComparison(String key, String digestValue, String fileName) {
		StringBuilder sb = new StringBuilder(String.format("Digesting completed. Digest of %s ", fileName));

		if (key.equals(digestValue)) {
			sb.append("matches expected digest.");
		} else {
			sb.append(String.format("does not match the expected digest.%n Digest was: %s", digestValue));
		}

		return sb.toString();
	}

	/**
	 * Calculates digest for given path file using SHA-256 algorithm.
	 * 
	 * @param path
	 *            Path to file.
	 * @return Digest.
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 *             If couldn't open file.
	 */

	private static String calculateSHA(Path path) throws NoSuchAlgorithmException, IOException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

		try (InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
			byte[] buffer = new byte[BUFFER_SIZE];

			while (true) {
				int r = is.read(buffer);
				if (r < 1)
					break;
				messageDigest.update(buffer, 0, r);
			}
		}

		return Util.bytetohex(messageDigest.digest());
	}
}

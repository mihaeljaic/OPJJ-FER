package hr.fer.zemris.java.webserver;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Web server that gets single argument that is path to server properties. In
 * this case use "./config/server.properties" as path. It provides displaying of
 * text, html, png, jpg and gif files. It can also run smart scripts see
 * {@link SmartScriptParser}. Following files can be launched in browser from
 * this server:<br>
 * <br>
 * <a href=
 * "http://127.0.0.1:5721/index.html">http://127.0.0.1:5721/index.html</a><br>
 * <a href=
 * "http://127.0.0.1:5721/fruits.png">http://127.0.0.1:5721/fruits.png</a><br>
 * <a href=
 * "http://127.0.0.1:5721/fruits.jpg">http://127.0.0.1:5721/fruits.jpg</a><br>
 * <a href=
 * "http://127.0.0.1:5721/sample.txt">http://127.0.0.1:5721/sample.txt</a><br>
 * <a href=
 * "http://127.0.0.1:5721/scripts/osnovni.smscr">http://127.0.0.1:5721/scripts/osnovni.smscr</a><br>
 * <a href=
 * "http://127.0.0.1:5721/scripts/zbrajanje.smscr?a=3&b=7">http://127.0.0.1:5721/scripts/zbrajanje.smscr?a=3&b=7</a><br>
 * <a href=
 * "http://127.0.0.1:5721/scripts/brojPoziva.smscr">http://127.0.0.1:5721/scripts/brojPoziva.smscr</a><br>
 * <a href=
 * "http://localhost:5721/scripts/brojPoziva.smscr">http://localhost:5721/scripts/brojPoziva.smscr</a><br>
 * <a href=
 * "http://127.0.0.1:5721/scripts/fibonacci.smscr">http://127.0.0.1:5721/scripts/fibonacci.smscr</a><br>
 * <a href="http://127.0.0.1:5721/hello">http://127.0.0.1:5721/hello</a><br>
 * <a href=
 * "http://127.0.0.1:5721/hello?name=john">http://127.0.0.1:5721/hello?name=john</a><br>
 * <a href="http://127.0.0.1:5721/cw">http://127.0.0.1:5721/cw</a><br>
 * <a href=
 * "http://127.0.0.1:5721/ext/HelloWorker">http://127.0.0.1:5721/ext/HelloWorker</a><br>
 * <a href=
 * "http://127.0.0.1:5721/ext/CircleWorker">http://127.0.0.1:5721/ext/CircleWorker</a><br>
 * <a href=
 * "http://127.0.0.1:5721/ext/EchoParams?name=name1&name2=name2&name3=name3">http://127.0.0.1:5721/ext/EchoParams?name=name1&name2=name2&name3=name3</a><br>
 * <a href=
 * "http://127.0.0.1:5721/calc?a=11&b=22">http://127.0.0.1:5721/calc?a=11&b=22</a><br>
 * <a href=
 * "http://127.0.0.1:5721/magikarp.gif">http://127.0.0.1:5721/magikarp.gif</a><br>
 * <br>
 * To shut down server type "stop".
 * 
 * @author Mihael Jaić
 *
 */

public class SmartHttpServer {
	/**
	 * Server address.
	 */
	private String address;
	/**
	 * Server port.
	 */
	private int port;
	/**
	 * Number of worker threads.
	 */
	private int workerThreads;
	/**
	 * Time before session cookie expires.
	 */
	private int sessionTimeout;
	/**
	 * Mime types map.
	 */
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	/**
	 * Server thread.
	 */
	private ServerThread serverThread;
	/**
	 * Client worker threads.
	 */
	private ExecutorService threadPool;
	/**
	 * Session deleter.
	 */
	private SessionDeleter sessionDeleter;
	/**
	 * Document root.
	 */
	private Path documentRoot;
	/**
	 * Flag that tells if server is running.
	 */
	private volatile boolean serverRunning = false;
	/**
	 * Mutex used for synchronization of client workers while checking cookie
	 * session.
	 */
	private volatile Object clientWorkerMutex = new Object();
	/**
	 * Workers.
	 */
	private Map<String, IWebWorker> workersMap = new HashMap<>();
	/**
	 * Sessions.
	 */
	private Map<String, SessionMapEntry> sessions = new HashMap<>();
	/**
	 * Used for generated session id.
	 */
	private Random sessionRandom = new Random();
	/**
	 * Server socket.
	 */
	private ServerSocket serverSocket;

	/**
	 * Constructor that gets path to server config file and loads server
	 * configuration needed for starting server.
	 * 
	 * @param configFileName
	 */

	public SmartHttpServer(String configFileName) {
		loadProperties(configFileName);
	}

	/**
	 * Starts server.
	 */

	protected synchronized void start() {
		System.out.println("Starting server...");
		if (serverRunning) {
			throw new IllegalStateException();
		}

		serverRunning = true;
		serverThread = new ServerThread();
		serverThread.start();

		sessionDeleter = new SessionDeleter();
		sessionDeleter.setDaemon(true);
		sessionDeleter.start();

		threadPool = Executors.newFixedThreadPool(workerThreads, runnable -> {
			Thread thread = new Thread(runnable);
			thread.setDaemon(true);
			return thread;
		});
	}

	/**
	 * Main method from where program starts.
	 * 
	 * @param args
	 *            Command line arguments.
	 */

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Expected path to server properties.");
			return;
		}

		SmartHttpServer webServer = new SmartHttpServer(args[0]);
		Scanner sc = new Scanner(System.in);

		webServer.start();

		System.out.printf("Type 'stop' to shut down server.%n%n");
		while (true) {
			String entry = sc.nextLine();
			if (entry.toLowerCase().equals("stop")) {
				break;
			}
		}

		webServer.stop();

		sc.close();
	}

	/**
	 * Stops server from running.
	 */

	protected synchronized void stop() {
		if (!serverRunning) {
			throw new IllegalStateException();
		}

		serverRunning = false;
		threadPool.shutdown();
		try {
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("Couldn't close server socket.");
		}
	}

	/**
	 * Server thread that waits for client's request and serves client. Uses
	 * {@link ClientWorker} threads to do job.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	protected class ServerThread extends Thread {
		@Override
		public void run() {
			try {
				serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), port));
			} catch (IOException e) {
				System.out.println("Error while creating server socket.");
				System.exit(0);
			}
			System.out.printf("Server successfully started.%n");

			while (true) {
				synchronized (SmartHttpServer.this) {
					if (!serverRunning) {
						System.out.println("Server stopped.");
						break;
					}
				}

				Socket client = null;
				try {
					client = serverSocket.accept();
				} catch (Exception e) {
					synchronized (SmartHttpServer.this) {
						if (!serverRunning) {
							System.out.println("Server stopped.");
							return;
						}
					}

					System.out.println("Couldn't accept client.");
					continue;
				}

				ClientWorker cw = new ClientWorker(client);
				threadPool.submit(cw);
			}
		}
	}

	/**
	 * Loads server, mime and worker properties.
	 * 
	 * @param configFileName
	 *            Path to server properties.
	 */

	private void loadProperties(String configFileName) {
		Properties properties = new Properties();
		try (InputStream inputStream = Files.newInputStream(Paths.get(configFileName), StandardOpenOption.READ)) {
			properties.load(inputStream);
		} catch (IOException e) {
			System.out.println("Couldn't open server properties file.");
			System.exit(0);
		}

		address = properties.getProperty("server.address");
		port = Integer.parseInt(properties.getProperty("server.port"));
		workerThreads = Integer.parseInt(properties.getProperty("server.workerThreads"));
		documentRoot = Paths.get(properties.getProperty("server.documentRoot"));
		sessionTimeout = Integer.parseInt(properties.getProperty("session.timeout"));

		try (InputStream inputStream = Files.newInputStream(Paths.get(properties.getProperty("server.mimeConfig")),
				StandardOpenOption.READ)) {
			Properties mimeProperties = new Properties();
			mimeProperties.load(inputStream);
			for (Object key : mimeProperties.keySet()) {
				mimeTypes.put((String) key, mimeProperties.getProperty((String) key));
			}
		} catch (IOException e) {
			System.out.println("Couldn't open mime properties.");
			System.exit(0);
		}

		loadWorkers(Paths.get(properties.getProperty("server.workers")));
	}

	/**
	 * Loads workers.
	 * 
	 * @param file
	 *            Path to worker's properties.
	 */

	private void loadWorkers(Path file) {
		try (BufferedReader bufferedReader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.startsWith("#") || line.trim().isEmpty()) {
					continue;
				}

				String[] parameters = line.split("=");
				if (parameters.length != 2) {
					System.out.println("Invalid data in workers properties.");
					System.exit(0);
				}

				String path = parameters[0].trim();
				String fqcn = parameters[1].trim();
				if (workersMap.containsKey(path)) {
					System.out.println("Duplicate path!");
					System.exit(0);
				}

				Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
				workersMap.put(path, (IWebWorker) referenceToClass.newInstance());
			}
		} catch (IOException e) {
			System.out.println("Couldn't open worker properties.");
			System.exit(0);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println(e.getMessage());
			System.out.println("Couldn't find worker class");
			System.exit(0);
		}
	}

	/**
	 * Client worker that processes client's request using
	 * {@link RequestContext}.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private class ClientWorker implements Runnable, IDispatcher {
		/**
		 * Client socket.
		 */
		private Socket csocket;
		/**
		 * Input stream.
		 */
		private PushbackInputStream istream;
		/**
		 * Output stream.
		 */
		private OutputStream ostream;
		/**
		 * Protocol version.
		 */
		private String version;
		/**
		 * Request method.
		 */
		private String method;
		/**
		 * Parameters.
		 */
		private Map<String, String> params = new HashMap<String, String>();
		/**
		 * Temporary parameters.
		 */
		private Map<String, String> tempParams = new HashMap<String, String>();
		/**
		 * Permanent parameters.
		 */
		private Map<String, String> permPrams;
		/**
		 * Output cookies.
		 */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		/**
		 * Session ID.
		 */
		private String SID;
		/**
		 * Buffer size.
		 */
		private static final int bufferSize = 4096;
		/**
		 * Bad request status number.
		 */
		private static final int badRequest = 400;
		/**
		 * Forbidden status number.
		 */
		private static final int forbidden = 403;
		/**
		 * File not found status number.
		 */
		private static final int notFound = 404;
		/**
		 * Internal server error status number.
		 */
		private static final int internalServerError = 500;
		/**
		 * Requested context.
		 */
		private RequestContext context;

		/**
		 * Constructor that gets client's socket.
		 * 
		 * @param csocket
		 *            Client's socket.
		 */

		public ClientWorker(Socket csocket) {
			super();
			if (csocket == null) {
				throw new IllegalArgumentException();
			}

			this.csocket = csocket;
		}

		@Override
		public void run() {
			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = csocket.getOutputStream();
			} catch (IOException e) {
				System.out.println("Client worker: couldn't open streams.");
				return;
			}

			List<String> request = readRequest();
			if (request.size() < 1) {
				error(badRequest, "Bad request");
				return;
			}

			String firstLine = request.get(0);
			String[] temp = firstLine.split(" ");
			if (temp.length != 3) {
				error(badRequest, "Bad request");
				return;
			}

			method = temp[0];
			String requestedPath = temp[1];
			version = temp[2];
			if (!method.toUpperCase().equals("GET")
					|| (!version.toUpperCase().equals("HTTP/1.0") && !version.toUpperCase().equals("HTTP/1.1"))) {
				error(badRequest, "Bad request");
				return;
			}

			checkSession(request);

			String domain = getDomain(request);
			outputCookies.add(new RCCookie("sid", SID, null, domain, "/"));

			String[] pathAndParameters = requestedPath.split("\\?");

			if (!parseParameters(pathAndParameters)) {
				error(badRequest, "Bad request");
				return;
			}

			if (checkForbidden(pathAndParameters[0])) {
				error(forbidden, "Forbidden");
				return;
			}

			try {
				internalDispatchRequest(pathAndParameters[0], true);
			} catch (Exception e1) {
				error(notFound, "File not found");
				return;
			}

			try {
				ostream.flush();
				csocket.close();
			} catch (IOException e) {
				System.out.println("Couldn't close client socket.");
			}
		}

		/**
		 * Checks if given path is forbidden. In other words path exists on
		 * server computer but isn't part of server files.
		 * 
		 * @param relativePath
		 *            Path.
		 * @return True if path is forbidden, false otherwise.
		 */

		private boolean checkForbidden(String relativePath) {
			Path absolutePath = Paths.get(relativePath);

			return !absolutePath.toAbsolutePath().toString().startsWith(documentRoot.toAbsolutePath().toString())
					&& Files.exists(absolutePath);
		}

		/**
		 * Checks if client already has installed session cookie. If it doesn't
		 * or if session expired new session cookie is made that is sent back to
		 * client.
		 * 
		 * @param request
		 */

		private void checkSession(List<String> request) {
			synchronized (clientWorkerMutex) {
				String sidCandidate = null;

				for (String line : request) {
					if (!line.startsWith("Cookie:")) {
						continue;
					}

					// Extracts sid.
					if (line.contains("sid")) {
						String[] temp = line.substring(line.indexOf("sid")).split("=");
						sidCandidate = temp[1].split(";")[0].trim();
						// Removes quotes.
						sidCandidate = sidCandidate.substring(1, sidCandidate.length() - 1);
						break;
					}
				}

				if (sidCandidate == null) {
					makeCookie();
					return;
				}

				SessionMapEntry mapEntry = sessions.get(sidCandidate);
				if (mapEntry == null || mapEntry.validUntil / 1000 >= System.currentTimeMillis() / 1000) {
					if (mapEntry != null) {
						sessions.remove(sidCandidate);
					}

					makeCookie();
					return;
				}

				mapEntry.validUntil = System.currentTimeMillis() / 1000 + (long) sessionTimeout;
				SID = sidCandidate;
				permPrams = mapEntry.map;
			}
		}

		/**
		 * Creates new cookie.
		 */

		private void makeCookie() {
			Map<String, String> newMap = new ConcurrentHashMap<>();

			final int sidLength = 20;
			StringBuilder sb = new StringBuilder(sidLength);
			for (int i = 0; i < sidLength; i++) {
				// Generates random uppercase letter.
				sb.append((char) (sessionRandom.nextInt('Z' - 'A' + 1) + 'A'));
			}

			SID = sb.toString();
			SessionMapEntry newSession = new SessionMapEntry();
			newSession.sid = SID;
			newSession.map = newMap;
			newSession.validUntil = System.currentTimeMillis() / 1000 + (long) sessionTimeout;
			sessions.put(new String(newSession.sid), newSession);

			permPrams = newMap;
			System.out.printf("Created new session cookie with sid: %s%n%n", SID);
		}

		/**
		 * Gets domain from client.
		 * 
		 * @param request
		 *            Request header.
		 * @return Server domain.
		 */

		private String getDomain(List<String> request) {
			for (String line : request) {
				if (!line.toUpperCase().startsWith("HOST")) {
					continue;
				}

				String[] temp = line.split(":");
				if (temp.length < 2) {
					error(badRequest, "Invalid host name");
				}
				return temp[1].trim();
			}

			return address;
		}

		/**
		 * If something went wrong this method is called to inform client about
		 * the error.
		 * 
		 * @param statusCode
		 *            Status code.
		 * @param statusText
		 *            Status text.
		 */

		private void error(int statusCode, String statusText) {
			try {
				if (context == null) {
					context = new RequestContext(ostream, params, permPrams, null);
					context.setMimeType(mimeTypes.get("html"));
					context.setWriteLogs(true);
				}

				context.write((String.format("%d %s%n", statusCode, statusText)));

			} catch (IOException e) {
				System.out.println("Couldn't display message");
			}
		}

		/**
		 * Parses parameters from client's request.
		 * 
		 * @param paramString
		 *            Parameter's string.
		 * @return True if parameters are valid, false otherwise.
		 */

		private boolean parseParameters(String[] paramString) {
			if (paramString.length != 2) {
				return paramString.length == 1;
			}

			String[] parameters = paramString[1].split("&");
			for (String parameter : parameters) {
				String[] parMapping = parameter.split("=");
				if (parMapping.length != 2) {
					return false;
				}

				params.put(parMapping[0], parMapping[1]);
			}

			return true;
		}

		/**
		 * Simple automata that reads header of client's request.
		 * 
		 * @return Request split in lines.
		 */

		private List<String> readRequest() {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
			l: while (true) {
				int b = 0;
				try {
					b = istream.read();
				} catch (IOException e) {
				}
				if (b == -1)
					return null;
				if (b != 13) {
					bos.write(b);
				}
				switch (state) {
				case 0:
					if (b == 13) {
						state = 1;
					} else if (b == 10)
						state = 4;
					break;
				case 1:
					if (b == 10) {
						state = 2;
					} else
						state = 0;
					break;
				case 2:
					if (b == 13) {
						state = 3;
					} else
						state = 0;
					break;
				case 3:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				case 4:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				}
			}
			String request = new String(bos.toByteArray(), StandardCharsets.UTF_8);
			System.out.printf("Received request:%n%s%n", request);

			return Arrays.asList(request.split("\\R"));
		}

		/**
		 * Processes client's request.
		 * 
		 * @param urlPath
		 *            Path to file on server.
		 * @param directCall
		 *            If call was direct.
		 * @throws Exception
		 *             If something went wrong during processing request.
		 */

		public void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			if (directCall && urlPath.startsWith("/private")) {
				error(forbidden, "You have no access to file");
				return;
			}

			if (context == null) {
				context = new RequestContext(ostream, params, permPrams, outputCookies, tempParams, this);
				context.setWriteLogs(true);
			}

			if (workersMap.containsKey(urlPath)) {
				IWebWorker worker = workersMap.get(urlPath);

				try {
					synchronized (worker) {
						worker.processRequest(context);
					}
				} catch (Exception e) {
					System.out.printf("Couldn't process request: %s%n%s%n%n", urlPath, e.getMessage());
					error(internalServerError, "Couldn't process request");
				}

			} else if (urlPath.endsWith(".smscr")) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				try (InputStream inputStream = Files.newInputStream(Paths.get(documentRoot.toAbsolutePath() + urlPath),
						StandardOpenOption.READ)) {
					byte[] buffer = new byte[bufferSize];
					while (true) {
						int readBytes = inputStream.read(buffer);
						if (readBytes < 1) {
							break;
						}

						bos.write(buffer, bos.size(), readBytes);
					}
				}

				String expression = new String(bos.toByteArray(), StandardCharsets.UTF_8);
				DocumentNode documentNode = null;
				try {
					documentNode = new SmartScriptParser(expression).getDocumentNode();
				} catch (SmartScriptParserException ex) {
					System.out.printf("Couldn't parse script: %s%n%s%n%n", urlPath, ex.getMessage());
					error(internalServerError, String.format("Couldn't parse script: %s", urlPath));
					return;
				}
				SmartScriptEngine scriptEngine = new SmartScriptEngine(documentNode, context);
				try {
					scriptEngine.execute();
				} catch (RuntimeException ex) {
					System.out.printf("Couldn't execute script: %s%n%s%n%n", urlPath, ex.getMessage());
					error(internalServerError, String.format("Couldn't execute script: %s", urlPath));
				}

			} else if (urlPath.startsWith("/ext/")) {
				Class<?> referenceToClass = this.getClass().getClassLoader()
						.loadClass("hr.fer.zemris.java.webserver.workers." + urlPath.split("/ext/")[1]);
				// If more client workers try to access same worker.
				synchronized (referenceToClass) {
					try {
						((IWebWorker) referenceToClass.newInstance()).processRequest(context);
					} catch (Exception ex) {
						System.out.printf("Couldn't process request: %s%n%s%n%n", urlPath, ex.getMessage());
						error(internalServerError, "Couldn't process request");
					}
				}

			} else {
				Path path = Paths.get(documentRoot.toString() + urlPath);
				if (!(path.toFile().isFile() && path.toFile().canRead() && urlPath.contains("."))) {
					error(notFound, String.format("File not found: %s", urlPath));
					return;
				}

				String fileExtension = urlPath.substring(urlPath.lastIndexOf(".") + 1);
				context.setMimeType(mimeTypes.get(fileExtension));
				context.write(Files.readAllBytes(path));
			}
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}

	}

	/**
	 * Thread that every 5 minutes deletes expired sessions.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private class SessionDeleter extends Thread {
		/**
		 * Time that thread spends sleeping until next check for expired
		 * sessions.
		 */
		private static final int sleepTime = 300000;

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException ignorable) {
				}

				boolean deleted = false;
				Iterator<Entry<String, SessionMapEntry>> it = sessions.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, SessionMapEntry> session = it.next();
					if (session.getValue().validUntil <= System.currentTimeMillis() / 1000) {
						System.out.printf("Removing session cookie: %s%n", session.getKey());
						deleted = true;
						it.remove();
					}
				}

				if (deleted) {
					System.out.println("");
				}
			}
		}
	}

	/**
	 * Structure that holds data for session.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	private static class SessionMapEntry {
		/**
		 * Session ID.
		 */
		String sid;
		/**
		 * Time until session is valid in seconds.
		 */
		long validUntil;
		/**
		 * Map in which parameters are stored for some session.
		 */
		Map<String, String> map;
	}
}

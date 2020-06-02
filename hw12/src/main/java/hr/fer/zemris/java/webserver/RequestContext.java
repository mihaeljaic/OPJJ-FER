package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Context that is sent to client by using methods write. Holds information
 * about current parameters, mime type, etc. When any of write methods is first
 * time called header is generated for client's browser.
 * 
 * @author Mihael Jaić
 *
 */

public class RequestContext {
	/**
	 * Output stream.
	 */
	private OutputStream outputStream;
	/**
	 * Charset.
	 */
	private Charset charset;
	/**
	 * Encoding.
	 */
	private String encoding = "UTF-8";
	/**
	 * Status code.
	 */
	private int statusCode = 200;
	/**
	 * Status text.
	 */
	private String statusText = "OK";
	/**
	 * Mime type.
	 */
	private String mimeType = "text/html";
	/**
	 * Parameters.
	 */
	private Map<String, String> parameters;
	/**
	 * Temporary parameters.
	 */
	private Map<String, String> temporaryParameters = new HashMap<>();
	/**
	 * Persistent parameters.
	 */
	private Map<String, String> persistentParameters;
	/**
	 * Output cookies.
	 */
	private List<RCCookie> outputCookies;
	/**
	 * Dispatcher.
	 */
	private IDispatcher dispatcher;
	/**
	 * Flag that marks if header was generated.
	 */
	private boolean headerGenerated = false;
	/**
	 * Flag that marks if logs have to printed on standard output.
	 */
	private boolean writeLogs;

	/**
	 * Constructor that gets most attributes.
	 * 
	 * @param outputStream
	 *            Output stream.
	 * @param parameters
	 *            Paramaters.
	 * @param persistentParameters
	 *            Persistent parameters.
	 * @param outputCookies
	 *            Output cookies.
	 * @throws IllegalArgumentException
	 *             If output stream is null.
	 */

	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) throws IllegalArgumentException {
		if (outputStream == null) {
			throw new IllegalArgumentException();
		}

		this.outputStream = outputStream;
		this.parameters = parameters == null ? new HashMap<>() : parameters;
		this.persistentParameters = persistentParameters == null ? new HashMap<>() : persistentParameters;
		this.outputCookies = outputCookies == null ? new ArrayList<>() : outputCookies;
	}

	/**
	 * Constructor that sets attributes.
	 * 
	 * @param outputStream
	 *            Output stream.
	 * @param parameters
	 *            Parameters.
	 * @param persistentParameters
	 *            Persistent parameters.
	 * @param outputCookies
	 *            Output cookies.
	 * @param temporaryParameters
	 *            Temporary parameters.
	 * @param dispatcher
	 *            Dispatcher
	 * @throws IllegalArgumentException
	 *             If output stream or dispatcher are null.
	 */

	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies,
			Map<String, String> temporaryParameters, IDispatcher dispatcher) throws IllegalArgumentException {
		this(outputStream, parameters, persistentParameters, outputCookies);

		if (dispatcher == null) {
			throw new IllegalArgumentException();
		}

		if (temporaryParameters != null) {
			this.temporaryParameters = temporaryParameters;
		}
		this.dispatcher = dispatcher;
	}

	/**
	 * Gets parameters
	 * 
	 * @return Parameters.
	 */

	public Map<String, String> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}

	/**
	 * Sets encoding.
	 * 
	 * @param encoding
	 *            Encoding.
	 * @throws IllegalArgumentException
	 *             If encoding is null.
	 * @throws RuntimeException
	 *             If header was already generated.
	 */

	public void setEncoding(String encoding) throws IllegalArgumentException, RuntimeException {
		if (encoding == null) {
			throw new IllegalArgumentException();
		}

		if (headerGenerated) {
			throw new RuntimeException("Header was already generated, can't change it's properties!");
		}

		this.encoding = encoding;
	}

	/**
	 * Sets status code.
	 * 
	 * @param statusCode
	 *            Status code.
	 * @throws RuntimeException
	 *             If header was already generated.
	 */

	public void setStatusCode(int statusCode) throws RuntimeException {
		if (headerGenerated) {
			throw new RuntimeException("Header was already generated, can't change it's properties!");
		}

		this.statusCode = statusCode;
	}

	/**
	 * Sets status text.
	 * 
	 * @param statusText
	 *            Status text.
	 * @throws IllegalArgumentException
	 *             If status text is null.
	 * @throws RuntimeException
	 *             If header was already generated.
	 */

	public void setStatusText(String statusText) throws IllegalArgumentException, RuntimeException {
		if (statusText == null) {
			throw new IllegalArgumentException();
		}

		if (headerGenerated) {
			throw new RuntimeException("Header was already generated, can't change it's properties!");
		}

		this.statusText = statusText;
	}

	/**
	 * Sets mimey type.
	 * 
	 * @param mimeType
	 *            Mime type.
	 * @throws IllegalArgumentException
	 *             If mime type is null.
	 * @throws RuntimeException
	 *             If header was already generated.
	 */

	public void setMimeType(String mimeType) throws IllegalArgumentException, RuntimeException {
		if (mimeType == null) {
			throw new IllegalArgumentException();
		}

		if (headerGenerated) {
			throw new RuntimeException("Header was already generated, can't change it's properties!");
		}

		this.mimeType = mimeType;
	}

	/**
	 * Adds cookie to output cookies.
	 * 
	 * @param cookie
	 *            Cookie.
	 * @throws IllegalArgumentException
	 *             If cookie is null.
	 * @throws RuntimeException
	 *             If header was already generated.
	 */

	public void addRCCookie(RCCookie cookie) throws IllegalArgumentException, RuntimeException {
		if (cookie == null) {
			throw new IllegalArgumentException();
		}

		if (headerGenerated) {
			throw new RuntimeException("Header was already generated, can't change it's properties!");
		}

		outputCookies.add(cookie);
	}

	/**
	 * Gets parameter's value by given name. Or null if there is no such
	 * parameter.
	 * 
	 * @param name
	 *            Name.
	 * @return Value Value.
	 */

	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Gets unmodifiable set of parameter names.
	 * 
	 * @return Unmodifiable set of parameter names.
	 */

	public Set<String> getParameterNames() {
		return extractKeys(parameters);
	}

	/**
	 * Extract keys from map into unmodifiable set.
	 * 
	 * @param map
	 *            Map.
	 * @return Unmodifiable set of keys from map.
	 */

	private Set<String> extractKeys(Map<String, String> map) {
		Set<String> tempSet = new HashSet<>();
		for (Map.Entry<String, String> paramEntry : map.entrySet()) {
			tempSet.add(paramEntry.getKey());
		}

		return Collections.unmodifiableSet(tempSet);
	}

	/**
	 * Gets persistent parameter from given name. If name doesn't exist in
	 * persistent parameters returns null.
	 * 
	 * @param name
	 *            Name.
	 * @return Persistent parameter value.
	 */

	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * Gets unmodifiable set of persistent parameter names.
	 * 
	 * @return Unmodifiable set of persistent parameter names.
	 */

	public Set<String> getPersistentParameterNames() {
		return extractKeys(persistentParameters);
	}

	/**
	 * Sets persistent parameter value.
	 * 
	 * @param name
	 *            Name of persistent parameter.
	 * @param value
	 *            Value of persistent parameter.
	 * @throws IllegalArgumentException
	 *             If name or value are null.
	 */

	public void setPersistentParameter(String name, String value) throws IllegalArgumentException {
		if (name == null || value == null) {
			throw new IllegalArgumentException();
		}

		persistentParameters.put(name, value);
	}

	/**
	 * Removes persistenet parameter.
	 * 
	 * @param name
	 *            Name of persistent parameter.
	 */

	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * Gets temporary parameter. If given name of temporary parameter doesn't
	 * exist returns null.
	 * 
	 * @param name
	 *            Name of temporary parameter.
	 * @return Value of temporary parameter.
	 */

	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}

	/**
	 * Gets unmodifiable set of temporary parameters' names.
	 * 
	 * @return Unmodifiable set of temporary parameters' names.
	 */

	public Set<String> getTemporaryParameterNames() {
		return extractKeys(temporaryParameters);
	}

	/**
	 * Sets temporary parameter.
	 * 
	 * @param name
	 *            Temporary parameter name.
	 * @param value
	 *            Temporary parameter value.
	 * @throws IllegalArgumentException
	 *             If name or value are null.
	 */

	public void setTemporaryParameter(String name, String value) throws IllegalArgumentException {
		if (name == null || value == null) {
			throw new IllegalArgumentException();
		}

		temporaryParameters.put(name, value);
	}

	/**
	 * Removes temporary parameter.
	 * 
	 * @param name
	 *            Temporary parameter name.
	 */

	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}

	/**
	 * Writes given data to output stream. If this is first time any of write
	 * methods is called header is generated.
	 * 
	 * @param data
	 *            Data.
	 * @return This context.
	 * @throws IOException
	 *             If I/O error occurred.
	 */

	public RequestContext write(byte[] data) throws IOException {
		if (!headerGenerated) {
			outputStream.write(createHeader());
		}

		outputStream.write(data);
		outputStream.flush();
		return this;
	}

	/**
	 * Writes given text to output stream. If this is first time any of write
	 * methods is called header is generated.
	 * 
	 * @param text
	 *            Text.
	 * @return This context.
	 * @throws IOException
	 *             If I/O error occurred.
	 */

	public RequestContext write(String text) throws IOException {
		if (!headerGenerated) {
			outputStream.write(createHeader());
		}

		outputStream.write(text.getBytes(charset));
		outputStream.flush();
		return this;
	}

	/**
	 * Creates header that is sent to client's browser.
	 * 
	 * @return Header in bytes.
	 */

	private byte[] createHeader() {
		charset = Charset.forName(encoding);

		StringBuilder sb = new StringBuilder(String.format("HTTP/1.1 %s %s\r\n", statusCode, statusText));
		sb.append(String.format("Content-Type: %s%s\r\n", mimeType,
				mimeType.startsWith("text/") ? ("; charset=" + encoding) : ""));
		for (RCCookie cookie : outputCookies) {
			String domain = cookie.domain == null ? "" : String.format("; Domain=%s", cookie.domain);
			String path = cookie.path == null ? "" : String.format("; Path=%s", cookie.path);
			String maxAge = cookie.maxAge == null ? "" : String.format("; Max-Age=%d", cookie.maxAge);
			sb.append(String.format("Set-Cookie: %s=\"%s\"%s%s%s; HttpOnly\r\n", cookie.name, cookie.value, domain,
					path, maxAge));
		}

		if (writeLogs) {
			System.out.printf("Sending response with header:%n%s%n", sb.toString());
		}
		sb.append("\r\n");
		headerGenerated = true;

		return sb.toString().getBytes(StandardCharsets.ISO_8859_1);
	}

	/**
	 * Sets if response's will be written on standard output.
	 * 
	 * @param writeLogs
	 *            If response will be written on standard output.
	 */

	public void setWriteLogs(boolean writeLogs) {
		this.writeLogs = writeLogs;
	}

	/**
	 * Gets dispatcher.
	 * 
	 * @return Dispatcher.
	 */

	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * Structure that stores information about cookies.
	 * 
	 * @author Mihael Jaić
	 *
	 */

	public static class RCCookie {
		/**
		 * Name.
		 */
		private String name;
		/**
		 * Value.
		 */
		private String value;
		/**
		 * Domain.
		 */
		private String domain;
		/**
		 * Path.
		 */
		private String path;
		/**
		 * Max age.
		 */
		private Integer maxAge;

		/**
		 * Constructor that sets attributes.
		 * 
		 * @param name
		 *            Name.
		 * @param value
		 *            Value.
		 * @param maxAge
		 *            Max age.
		 * @param domain
		 *            Domain.
		 * @param path
		 *            Path.
		 * @throws IllegalArgumentException
		 *             If name or value are null.
		 */

		public RCCookie(String name, String value, Integer maxAge, String domain, String path)
				throws IllegalArgumentException {
			if (name == null || value == null) {
				throw new IllegalArgumentException();
			}

			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * Gets name.
		 * 
		 * @return Name.
		 */

		public String getName() {
			return name;
		}

		/**
		 * Gets value.
		 * 
		 * @return Value.
		 */

		public String getValue() {
			return value;
		}

		/**
		 * Gets domain.
		 * 
		 * @return Domain.
		 */

		public String getDomain() {
			return domain;
		}

		/**
		 * Gets path.
		 * 
		 * @return Path.
		 */

		public String getPath() {
			return path;
		}

		/**
		 * Gets max age.
		 * 
		 * @return Max age.
		 */

		public Integer getMaxAge() {
			return maxAge;
		}
	}
}

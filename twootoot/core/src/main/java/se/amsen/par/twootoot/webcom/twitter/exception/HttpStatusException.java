package se.amsen.par.twootoot.webcom.twitter.exception;

/**
 * Exception for common Twitter error codes.
 *
 * @author params on 04/11/15
 */
public class HttpStatusException extends Exception {
	public static int HTTP_UNAUTHORIZED = 401;
	public static int HTTP_TOO_MANY_REQUESTS = 429;

	private int statusCode;

	public HttpStatusException(String message, int statusCode) {
		super(message);

		this.statusCode = statusCode;
	}

	public HttpStatusException(String message, int statusCode, Throwable ex) {
		super(message, ex);

		this.statusCode = statusCode;
	}

	public boolean isUnauthorized() {
		return statusCode == HTTP_UNAUTHORIZED;
	}

	public boolean isTooManyRequests() {
		return statusCode == HTTP_TOO_MANY_REQUESTS;
	}

	public int getStatusCode() {
		return statusCode;
	}
}

package se.amsen.par.twootoot.webcom.twitter.exception;

/**
 * Exception for when something unexpected happened during HTTP logic
 *
 * @author params on 04/11/15
 */
public class NetworkException extends Exception {
	public NetworkException() {
	}

	public NetworkException(String detailMessage) {
		super(detailMessage);
	}

	public NetworkException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NetworkException(Throwable throwable) {
		super(throwable);
	}
}

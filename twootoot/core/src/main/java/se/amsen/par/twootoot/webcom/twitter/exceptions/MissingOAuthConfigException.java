package se.amsen.par.twootoot.webcom.twitter.exceptions;

/**
 * @author params on 04/11/15
 */
public class MissingOAuthConfigException extends Exception {
	public MissingOAuthConfigException() {
	}

	public MissingOAuthConfigException(String detailMessage) {
		super(detailMessage);
	}

	public MissingOAuthConfigException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public MissingOAuthConfigException(Throwable throwable) {
		super(throwable);
	}
}

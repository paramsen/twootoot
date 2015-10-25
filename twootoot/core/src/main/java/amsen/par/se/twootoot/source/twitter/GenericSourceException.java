package amsen.par.se.twootoot.source.twitter;

/**
 * @author params on 25/10/15
 */
public class GenericSourceException extends Exception {
	public GenericSourceException() {
	}

	public GenericSourceException(String detailMessage) {
		super(detailMessage);
	}

	public GenericSourceException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public GenericSourceException(Throwable throwable) {
		super(throwable);
	}
}

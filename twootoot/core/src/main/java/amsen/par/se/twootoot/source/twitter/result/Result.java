package amsen.par.se.twootoot.source.twitter.result;

/**
 * Base Result for use in inter thread communication and Exception handling. A Source is responsible
 * for calling the provided Callback with a Result.
 *
 * @author params on 25/10/15
 */
public abstract class Result<T> {

	public Success<T> asSuccess() {
		return (Success<T>) this;
	}

	public Failure<T> asFailure() {
		return (Failure<T>) this;
	}

	public boolean isSuccess() {
		return this instanceof Success;
	}

	public boolean isFailure() {
		return this instanceof Failure;
	}
}

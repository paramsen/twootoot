package se.amsen.par.twootoot.source.result;

/**
 * Base Result for use in inter thread communication and Exception handling. A Source is responsible
 * for calling the provided Callback with a Result.
 *
 * Results are used to minimize the complexity introduced by Exceptions.
 * Ex. when a AsyncRunner has finished and calls Callback.onComplete(Result) on UI thread,
 * Callback.onComplete must check Result.isSuccess. If Result.isSuccess == false an Exception is
 * wrapped and can be retrieved by calling Result.asFailure().get().
 * This also means that when AsyncRunner fails with an Exception it will not silently die with its
 * owning Thread, but report back to Callback.onComplete with a Failure.
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
		return this instanceof Success && asSuccess().get() != null;
	}

	/**
	 * Do
	 */
	public boolean isSuccessIgnoreValue() {
		return this instanceof Success;
	}

	public boolean isFailure() {
		return this instanceof Failure && asFailure().get() != null;
	}
}

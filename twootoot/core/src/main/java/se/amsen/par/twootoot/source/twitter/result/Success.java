package se.amsen.par.twootoot.source.twitter.result;

/**
 * Generic Success wrapper
 *
 * @author params on 25/10/15
 */
public class Success<T> extends Result<T> {
	private T result;

	public Success() {
	}

	public Success(T result) {
		this.result = result;
	}

	public T get() {
		return result;
	}

	public void set(T result) {
		this.result = result;
	}
}

package amsen.par.se.twootoot.source.twitter.result;

/**
 * Exception wrapper
 *
 * @author params on 25/10/15
 */
public class Failure<T> extends Result<T> {
	private Throwable ex;

	public Failure() {
	}

	public Failure(Throwable ex) {
		this.ex = ex;
	}

	public Failure(Failure inner) {
		this.ex = inner.get();
	}

	public Throwable get() {
		return ex;
	}

	public void set(Throwable ex) {
		this.ex = ex;
	}
}

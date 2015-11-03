package se.amsen.par.twootoot.util.functional;

/**
 * Callbacks to be invoked by asynchronous functions.
 *
 * @author params on 25/10/15
 */
public interface Callback<T> {
	void onComplete(T result);
	//TODO void onProgress(Progress<T> progress);
}

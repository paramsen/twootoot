package se.amsen.par.twootoot.util.functional;

import se.amsen.par.twootoot.source.twitter.result.Result;

/**
 * Callbacks to be invoked by asynchronous functions.
 *
 * @author params on 25/10/15
 */
public interface Callback<T> {
	void onComplete(Result<T> result);
	//TODO void onProgress(Progress<T> progress);
}

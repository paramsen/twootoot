package amsen.par.se.twootoot.util.functional;

import amsen.par.se.twootoot.source.twitter.result.Result;

/**
 * Callbacks to be invoked by asynchronous functions.
 *
 * @author params on 25/10/15
 */
public interface Callback<T> {
	void onComplete(Result<T> result);
	//TODO void onProgress(Progress<T> progress);
}

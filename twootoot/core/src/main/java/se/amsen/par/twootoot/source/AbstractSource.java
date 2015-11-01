package se.amsen.par.twootoot.source;

import android.os.AsyncTask;

import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.source.twitter.result.Result;

/**
 * A AbstractSource is responsible for building and providing the model wrapped as a Result<?>. If a problem
 * occurs executing getResultN the returned Result should be a wrapped Failure (which is a wrapper
 * for an Exception).
 *
 * Use getResultNAsync to get the Result delivered to the provided Callback. Use getResultN to
 * get a Model synchronously, i.e. blocking.
 *
 * This class is built in a functional pattern meant to be as generic as possible. If need for more
 * than two parameter Types arise it would be viable to restructure this.
 *
 * @author params on 25/10/15
 */
public abstract class AbstractSource<Result1 extends Result, Param1, Param2> {
	/**
	 * Reset the AbstractSource. Ex. if the AbstractSource keeps a cache it should clear it.
	 *
	 * @return true if invalidation was successful
	 */
	public abstract boolean invalidate();

	/**
	 * Wrap getResult1 in an AsyncTask. When the AsyncTask is finished/cancelled the provided
	 * Callback.onComplete(Result) will be called (with the result from getResult1).
	 *
	 * @param p1 First param for getResult1
	 * @param callback The Callback to get called when AsyncTask is finished/failed
	 */
	protected void getResult1Async(final Param1 p1, final Callback<Result1> callback) {
		new AsyncTask<Void, Void, Result<Result1>>() {
			@Override
			protected void onPostExecute(Result<Result1> result) {
				callback.onComplete(result);
			}

			@Override
			protected Result1 doInBackground(Void... voids) {
				return getResult1(p1);
			}

			@Override
			protected void onCancelled(Result<Result1> result) {
				callback.onComplete(result);
			}
		}.execute();
	}

	/**
	 * Wrap getResultN in an AsyncTask. See getResult1Async.
	 */
	protected void getResult2Async(final Param1 p1, final Param2 p2, final Callback<Result1> callback) {
		new AsyncTask<Void, Void, Result<Result1>>() {
			@Override
			protected void onPostExecute(Result<Result1> result) {
				callback.onComplete(result);
			}

			@Override
			protected Result1 doInBackground(Void... voids) {
				return getResult2(p1, p2);
			}

			@Override
			protected void onCancelled(Result<Result1> result) {
				callback.onComplete(result);
			}
		}.execute();
	}

	/**
	 * Get Result with one parameter. This should always be synchronous and is responsible for
	 * not forking new threads that take part in constructing the Result.
	 *
	 * @param p1 Single param
	 * @return A Success object or a Failure.
	 */
	protected Result1 getResult1(Param1 p1) {
		throw new RuntimeException("Not supported by Source");
	}

	/**
	 * Get Result with N parameter. See getResult1.
	 */
	protected Result1 getResult2(Param1 p1, Param2 p2) {
		throw new RuntimeException("Not supported by Source");
	}
}

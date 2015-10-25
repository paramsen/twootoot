package amsen.par.se.twootoot.source.twitter;

import android.os.AsyncTask;

import amsen.par.se.twootoot.boilerplate.Callback;
import amsen.par.se.twootoot.source.twitter.result.Result;

/**
 * A Source is responsible for building and providing the model wrapped as a Result<?>. If a problem
 * occurs executing getResultN the returned Result should be a wrapped Failure (which is a wrapper
 * for an Exception).
 *
 * Use getResultNAsync to get the Result delivered to the provided Callback. Use getResultN to
 * get a Model synchronously, i.e. blocking.
 *
 * This class is built in a functional pattern meant to be as generic as possible.
 *
 * @author params on 25/10/15
 */
public abstract class Source<T extends Result> {
	/**
	 * Reset the Source. Ex. if the Source keeps a cache it should clear it.
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
	 * @param <S> Type of first param
	 */
	protected <S> void getResult1Async(final S p1, final Callback<T> callback) {
		new AsyncTask<Void, Void, Result<T>>() {
			@Override
			protected void onPostExecute(Result<T> result) {
				callback.onComplete(result);
			}

			@Override
			protected Result<T> doInBackground(Void... voids) {
				return getResult1(p1);
			}

			@Override
			protected void onCancelled(Result<T> result) {
				callback.onComplete(result);
			}
		}.execute();
	}

	/**
	 * Wrap getResultN in an AsyncTask. See getResult1Async.
	 */
	protected <S, R> void getResult2Async(final S p1, final R p2, final Callback<T> callback) {
		new AsyncTask<Void, Void, Result<T>>() {
			@Override
			protected void onPostExecute(Result<T> result) {
				callback.onComplete(result);
			}

			@Override
			protected Result<T> doInBackground(Void... voids) {
				return getResult2(p1, p2);
			}

			@Override
			protected void onCancelled(Result<T> result) {
				callback.onComplete(result);
			}
		}.execute();
	}

	/**
	 * Get Result with one parameter. This should always be synchronous and is responsible for
	 * not forking new threads that take part in constructing the Result.
	 *
	 * @param p1 Single param
	 * @param <S> Type of p1
	 * @return A Success object or a Failure.
	 */
	protected <S> Result<T> getResult1(S p1) {
		throw new RuntimeException("Not supported by Source");
	}

	/**
	 * Get Result with N parameter. See getResult1.
	 */
	protected <S, R> Result<T> getResult2(S p1, R p2) {
		throw new RuntimeException("Not supported by Source");
	}
}
